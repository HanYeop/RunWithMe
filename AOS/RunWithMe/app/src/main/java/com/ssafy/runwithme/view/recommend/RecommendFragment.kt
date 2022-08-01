package com.ssafy.runwithme.view.recommend

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentRecommendBinding
import com.ssafy.runwithme.model.response.RecommendResponse
import com.ssafy.runwithme.utils.FASTEST_LOCATION_UPDATE_INTERVAL
import com.ssafy.runwithme.utils.LOCATION_UPDATE_INTERVAL
import com.ssafy.runwithme.utils.TAG
import com.ssafy.runwithme.utils.TrackingUtility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*

@AndroidEntryPoint
class RecommendFragment : BaseFragment<FragmentRecommendBinding>(R.layout.fragment_recommend),
    OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private val recommendViewModel by viewModels<RecommendViewModel>()

    private lateinit var map: GoogleMap

    private lateinit var currentPosition: LatLng

    private var myLocation = Location("MyLocation")

    private var currentMarker: Marker? = null

    private lateinit var visibleRegion: VisibleRegion

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    // 처음 여부 (true = 아직 처음)
    private var first: Boolean = true

    override fun onMapReady(p0: GoogleMap) {
        map = p0

        updateLocation()

        map.setOnMarkerClickListener(this)
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        binding.apply {
            cardInfo.visibility = View.VISIBLE
            tvTitle.text = p0.title
            recommend = p0.tag as RecommendResponse
        }
        return true
    }

    override fun init() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_view_user) as SupportMapFragment
        mapFragment.getMapAsync(this)
        
        initViewModelCallBack()
    }

    private fun initViewModelCallBack(){
        lifecycleScope.launch { 
            recommendViewModel.recommendList.collectLatest {
                recommendDraw(it)
            }
        }
    }

    // 추천 장소 마커 찍기
    private fun recommendDraw(list: List<RecommendResponse>){
        for(i in list){
            val latLng = LatLng(i.runRecordDto.runRecordRunningLat, i.runRecordDto.runRecordRunningLng)
            val title = i.runRecordDto.runRecordEndTime
            var markerSnippet = getCurrentAddress(latLng)

            drawMarker(latLng, title, markerSnippet, i)
        }
    }

    // 위치 정보 요청하기
    @SuppressLint("MissingPermission")
    private fun updateLocation() {
        if (TrackingUtility.hasLocationPermissions(requireContext())) {
            val request = LocationRequest.create().apply {
                interval = LOCATION_UPDATE_INTERVAL // 위치 업데이트 주기
                fastestInterval = FASTEST_LOCATION_UPDATE_INTERVAL // 가장 빠른 위치 업데이트 주기
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY // 배터리소모를 고려하지 않으며 정확도를 최우선으로 고려
                maxWaitTime = LOCATION_UPDATE_INTERVAL // 최대 대기시간
            }

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
            fusedLocationProviderClient.requestLocationUpdates(request, locationCallback, Looper.getMainLooper())
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)

            result.locations.let {
                if(it.size > 0){
                    if(first) {
                        val location = it[it.size - 1]
                        currentPosition = LatLng(location.latitude, location.longitude)
                        var markerTitle = "내 위치"
                        var markerSnippet = getCurrentAddress(currentPosition)

                        myLocation.apply {
                            latitude = location.latitude; longitude = location.longitude
                        }

                        currentMarker = currentLocationDrawMarker(currentPosition, markerTitle, markerSnippet)
                        moveCamera(currentPosition)

                        first = false

                        if(binding.progressBarRecommend.visibility == View.VISIBLE){
                            binding.progressBarRecommend.visibility = View.GONE
                        }
                        if(binding.mapViewUser.visibility == View.INVISIBLE){
                            binding.mapViewUser.visibility = View.VISIBLE
                        }

                        visibleRegion = map.projection.visibleRegion

                        recommendViewModel.getRecommends(
                            visibleRegion.farLeft.longitude,
                            visibleRegion.nearLeft.latitude,
                            visibleRegion.nearRight.longitude,
                            visibleRegion.farRight.latitude)
                        Log.d("test5", "onLocationResult: ${visibleRegion.farLeft.longitude}")
                        Log.d("test5", "onLocationResult: ${visibleRegion.nearLeft.latitude}")
                        Log.d("test5", "onLocationResult: ${visibleRegion.nearRight.longitude}")
                        Log.d("test5", "onLocationResult: ${visibleRegion.farRight.latitude}")
                    }
                }
            }
        }
    }

    // 지도 카메라 움직이기
    private fun moveCamera(latLng: LatLng) {
        map.moveCamera(
            CameraUpdateFactory.newLatLngZoom(latLng, 14.5f)
        )
    }

    // 유저 현재 위치 마커 그리기
    private fun currentLocationDrawMarker(latLng: LatLng, markerTitle: String?, markerSnippet: String?): Marker? {
        val markerOptions = MarkerOptions().apply {
            position(latLng)
            title(markerTitle)
            snippet(markerSnippet)
            draggable(true)
            icon(
                BitmapDescriptorFactory.fromBitmap(
                    Bitmap.createScaledBitmap(
                        (ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.ic_marker,
                            requireContext().theme
                        ) as BitmapDrawable).bitmap,
                        50, 75, false
                    )
                )
            )
        }

        return map.addMarker(markerOptions)
    }

    // 마커 그리기
    private fun drawMarker(latLng: LatLng, markerTitle: String?, markerSnippet: String?, data: RecommendResponse): Marker? {
        val markerOptions = MarkerOptions().apply {
            position(latLng)
            title(markerTitle)
            snippet(markerSnippet)
            draggable(true)
            icon(
                BitmapDescriptorFactory.fromBitmap(
                    Bitmap.createScaledBitmap(
                        (ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.user_location,
                            requireContext().theme
                        ) as BitmapDrawable).bitmap,
                        150, 150, false
                    )
                )
            )
        }

        val marker = map.addMarker(markerOptions)
        marker.tag = data
        return marker
    }

    // 위도, 경도를 주소로 변환
    fun getCurrentAddress(latLng: LatLng): String {
        //지오코더: GPS를 주소로 변환
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses: List<Address>?
        try {
            addresses = geocoder.getFromLocation(
                latLng.latitude,
                latLng.longitude,
                1
            )
        } catch (ioException: IOException) {
            //네트워크 문제
//            Toast.makeText(requireContext(), "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show()
            return "지오코더 사용불가"
        } catch (illegalArgumentException: IllegalArgumentException) {
//            Toast.makeText(requireContext(), "잘못된 GPS 좌표", Toast.LENGTH_LONG).show()
            return "잘못된 GPS 좌표"
        }

        return if (addresses == null || addresses.isEmpty()) {
//            Toast.makeText(requireContext(), "주소 발견 불가", Toast.LENGTH_LONG).show()
            "주소 발견 불가"
        } else {
            val address = addresses[0]
            address.getAddressLine(0).toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }
}