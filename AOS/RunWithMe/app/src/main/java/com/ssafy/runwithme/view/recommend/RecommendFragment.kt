package com.ssafy.runwithme.view.recommend

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Looper
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragmentKeep
import com.ssafy.runwithme.databinding.FragmentRecommendBinding
import com.ssafy.runwithme.model.dto.*
import com.ssafy.runwithme.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*

@SuppressLint("MissingPermission")
@AndroidEntryPoint
class RecommendFragment : BaseFragmentKeep<FragmentRecommendBinding>(R.layout.fragment_recommend),
    OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private val recommendViewModel by viewModels<RecommendViewModel>()

    private lateinit var map: GoogleMap

    private lateinit var currentPosition: LatLng

    private var myLocation = Location("MyLocation")

    private var POLYLINE_COLOR = Color.RED

    private var polyLineList = mutableListOf<LatLng>()

    private lateinit var visibleRegion: VisibleRegion

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private var trackBoardList = listOf<TrackBoardFileDto>()

    private lateinit var currentRunRecord : RunRecordDto
    private lateinit var currentTrackBoard : TrackBoardDto
    private lateinit var currentImageFileDto : ImageFileDto

    private var job: Job = Job()

    // ?????? ?????? (true = ?????? ??????)
    private var first: Boolean = true

    override fun onMapReady(p0: GoogleMap) {
        map = p0

        p0.mapType = 1
        p0.isMyLocationEnabled = true

        map.setMinZoomPreference(14.5f)

        updateLocation()

        map.setOnMarkerClickListener(this)
    }

    override fun onMarkerClick(p0: Marker): Boolean {

        if(job.isActive){
            job.cancel()
        }

        binding.apply {
            cardInfo.visibility = View.VISIBLE
            tvTitle.text = p0.title
            val curTrackBoardFile = p0.tag as TrackBoardFileDto
            trackBoard = curTrackBoardFile
            p0.showInfoWindow()

            currentRunRecord = curTrackBoardFile.runRecordDto
            currentTrackBoard = curTrackBoardFile.trackBoardDto
            currentImageFileDto = curTrackBoardFile.trackBoardImageFileDto
        }
        recommendViewModel.getCoordinates((p0.tag as TrackBoardFileDto).runRecordDto.runRecordSeq)
        return true
    }

    override fun init() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_view_user) as SupportMapFragment
        mapFragment.getMapAsync(this)
        
        initViewModelCallBack()

        initClickListener()
    }

    private fun initClickListener(){
        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            cardInfo.setOnClickListener {
                val action = RecommendFragmentDirections.actionRecommendFragmentToRecommendDetailFragment(currentRunRecord, currentTrackBoard, currentImageFileDto.imgSeq)
                findNavController().navigate(action)
            }
            btnReSearch.setOnClickListener {
                visibleSearch()
            }
        }
    }

    private fun initViewModelCallBack(){
        lifecycleScope.launch { 
            recommendViewModel.trackBoardList.collectLatest {
                if(::map.isInitialized){
                    map.clear()
                }

                trackBoardList = it
                recommendDraw(trackBoardList)
                binding.tvTitleNum.text = it.size.toString()
            }
        }
        lifecycleScope.launch {
            recommendViewModel.getCoordinates.collectLatest {
                polyLineList = mutableListOf<LatLng>()
                initPolyLine(it)
            }
        }
    }

    private fun initPolyLine(list: List<CoordinateDto>){
        if(list.isNotEmpty()) {
            if(::map.isInitialized){
                map.clear()
            }
            for (i in list) {
                polyLineList.add(LatLng(i.latitude, i.longitude))
            }
            addAllPolylines()
            zoomToWholeTrack()
        }
    }

    // ?????? ?????? ??????
    private fun addAllPolylines() {
        job = lifecycleScope.launch {
            for(i in 1 until polyLineList.size){
                val polylineOptions = PolylineOptions()
                    .color(POLYLINE_COLOR)
                    .width(POLYLINE_WIDTH)
                    .add(polyLineList[i])
                    .add(polyLineList[i - 1])
                map.addPolyline(polylineOptions)
                delay(POLYLINE_DRAW_TIME)
            }
            if(::map.isInitialized){
                recommendDraw(trackBoardList)
            }
        }
    }

    // ?????? ????????? ??? ????????? ???
    private fun zoomToWholeTrack() {
        val bounds = LatLngBounds.Builder()
        for (polyline in polyLineList) {
            bounds.include(polyline)
        }

        val width = binding.mapViewUser.width
        val height = binding.mapViewUser.height
        map.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds.build(),
                width,
                height,
                (height * 0.05f).toInt()
            )
        )
    }

    // ?????? ?????? ?????? ??????
    private fun recommendDraw(list: List<TrackBoardFileDto>){
        for(i in list){
            val latLng = LatLng(i.runRecordDto.runRecordRunningLat, i.runRecordDto.runRecordRunningLng)
            val title = "${i.runRecordDto.userName} ?????? ?????? ??????"
            var markerSnippet = getCurrentAddress(latLng)

            drawMarker(latLng, title, markerSnippet, i)
        }
    }

    // ?????? ?????? ????????????
    @SuppressLint("MissingPermission")
    private fun updateLocation() {
        if (TrackingUtility.hasLocationPermissions(requireContext())) {
            val request = LocationRequest.create().apply {
                interval = LOCATION_UPDATE_INTERVAL // ?????? ???????????? ??????
                fastestInterval = FASTEST_LOCATION_UPDATE_INTERVAL // ?????? ?????? ?????? ???????????? ??????
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY // ?????????????????? ???????????? ????????? ???????????? ??????????????? ??????
                maxWaitTime = LOCATION_UPDATE_INTERVAL // ?????? ????????????
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

                        myLocation.apply {
                            latitude = location.latitude; longitude = location.longitude
                        }

                        moveCamera(currentPosition)

                        first = false

                        if(binding.progressBarRecommend.visibility == View.VISIBLE){
                            binding.progressBarRecommend.visibility = View.GONE
                        }
                        if(binding.mapViewUser.visibility == View.INVISIBLE){
                            binding.mapViewUser.visibility = View.VISIBLE
                        }
                        if(binding.btnReSearch.visibility == View.GONE){
                            binding.btnReSearch.visibility = View.VISIBLE
                        }
                        visibleSearch()
                    }
                }
            }
        }
    }

    // ?????? ??? ??????
    private fun visibleSearch(){
        if(binding.cardInfo.visibility == View.VISIBLE){
            binding.cardInfo.visibility = View.GONE
        }
        visibleRegion = map.projection.visibleRegion
        recommendViewModel.getRecommends(
            visibleRegion.farLeft.longitude,
            visibleRegion.nearLeft.latitude,
            visibleRegion.nearRight.longitude,
            visibleRegion.farRight.latitude)
    }

    // ?????? ????????? ????????????
    private fun moveCamera(latLng: LatLng) {
        map.moveCamera(
            CameraUpdateFactory.newLatLngZoom(latLng, 15.5f)
        )
    }

    // ?????? ?????????
    private fun drawMarker(latLng: LatLng, markerTitle: String?, markerSnippet: String?, data: TrackBoardFileDto): Marker? {
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

    // ??????, ????????? ????????? ??????
    fun getCurrentAddress(latLng: LatLng): String {
        //????????????: GPS??? ????????? ??????
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses: List<Address>?
        try {
            addresses = geocoder.getFromLocation(
                latLng.latitude,
                latLng.longitude,
                1
            )
        } catch (ioException: IOException) {
            //???????????? ??????
//            Toast.makeText(requireContext(), "???????????? ????????? ????????????", Toast.LENGTH_LONG).show()
            return "???????????? ????????????"
        } catch (illegalArgumentException: IllegalArgumentException) {
//            Toast.makeText(requireContext(), "????????? GPS ??????", Toast.LENGTH_LONG).show()
            return "????????? GPS ??????"
        }

        return if (addresses == null || addresses.isEmpty()) {
//            Toast.makeText(requireContext(), "?????? ?????? ??????", Toast.LENGTH_LONG).show()
            "?????? ?????? ??????"
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