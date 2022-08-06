package com.ssafy.runwithme.view.route_detail

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentRunningRouteBinding
import com.ssafy.runwithme.utils.FASTEST_LOCATION_UPDATE_INTERVAL
import com.ssafy.runwithme.utils.LOCATION_UPDATE_INTERVAL
import com.ssafy.runwithme.utils.POLYLINE_WIDTH
import com.ssafy.runwithme.utils.TrackingUtility
import com.ssafy.runwithme.view.running.RunningActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RunningRouteFragment : BaseFragment<FragmentRunningRouteBinding>(R.layout.fragment_running_route),
    OnMapReadyCallback{

    private lateinit var map: GoogleMap

    private var POLYLINE_COLOR = Color.RED

    private var first = true

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        POLYLINE_COLOR = resources.getColor(R.color.main_purple)
    }

    override fun init() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_view_route) as SupportMapFragment
        mapFragment.getMapAsync(this)

        initClickListener()
    }

    private fun initClickListener(){
        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        map = p0

        Log.d("test5", "onMapReady:")
        Log.d("test5", "onMapReady: ${RunningActivity.runPathPoints}")

        addAllPolylines()
        zoomToWholeTrack()

//        updateLocation()
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
                        addAllPolylines()
                        zoomToWholeTrack()

                        first = false
                    }
                }
            }
        }
    }

    // 경로 전부 표시
    private fun addAllPolylines() {
        Log.d("test5", "addAllPolylines: ${RunningActivity.runPathPoints}")
        lifecycleScope.launch {
            for (polyline in RunningActivity.runPathPoints) {
                for(i in 1 until polyline.size){
                    val polylineOptions = PolylineOptions()
                        .color(POLYLINE_COLOR)
                        .width(POLYLINE_WIDTH)
                        .add(polyline[i])
                        .add(polyline[i - 1])
                    map.addPolyline(polylineOptions)
                    delay(100)
                }
            }
        }
    }

    // 스냅샷 찍기 위하여 전체 경로가 다 보이게 줌
    private fun zoomToWholeTrack() {
        val bounds = LatLngBounds.Builder()
        for (polyline in RunningActivity.runPathPoints) {
            for (point in polyline) {
                bounds.include(point)
            }
        }
        val width = binding.mapViewRoute.width
        val height = binding.mapViewRoute.height
        map.moveCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds.build(),
                width,
                height,
                (height * 0.05f).toInt()
            )
        )
    }
}