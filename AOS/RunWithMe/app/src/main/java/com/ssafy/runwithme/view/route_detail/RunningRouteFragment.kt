package com.ssafy.runwithme.view.route_detail

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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
import com.ssafy.runwithme.model.dto.CoordinateDto
import com.ssafy.runwithme.utils.POLYLINE_DRAW_TIME
import com.ssafy.runwithme.utils.POLYLINE_WIDTH
import com.ssafy.runwithme.view.running.RunningViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RunningRouteFragment : BaseFragment<FragmentRunningRouteBinding>(R.layout.fragment_running_route),
    OnMapReadyCallback{

    private val runningViewModel by viewModels<RunningViewModel>()
    private val args by navArgs<RunningRouteFragmentArgs>()

    private lateinit var map: GoogleMap

    private var POLYLINE_COLOR = Color.RED

    private var polyLineList = mutableListOf<LatLng>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        POLYLINE_COLOR = resources.getColor(R.color.main_blue)
    }

    override fun init() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_view_route) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.apply {
            tvDistance.text = args.distancetext
            tvTime.text = args.timetext
        }

        initClickListener()
    }

    private fun initClickListener(){
        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun initViewModelCallBack(){
        lifecycleScope.launch {
            runningViewModel.getCoordinates.collectLatest {
                initPolyLine(it)
            }
        }
    }

    private fun initPolyLine(list: List<CoordinateDto>){
        if(list.isNotEmpty()) {
            for (i in list) {
                polyLineList.add(LatLng(i.latitude, i.longitude))
            }
            addAllPolylines()
            zoomToWholeTrack()
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        map = p0

        runningViewModel.getCoordinates(args.recordseq)
//        runningViewModel.getCoordinates(74)
        initViewModelCallBack()
    }

    // 경로 전부 표시
    private fun addAllPolylines() {
        lifecycleScope.launch {
            for(i in 1 until polyLineList.size){
                val polylineOptions = PolylineOptions()
                    .color(POLYLINE_COLOR)
                    .width(POLYLINE_WIDTH)
                    .add(polyLineList[i])
                    .add(polyLineList[i - 1])
                map.addPolyline(polylineOptions)
                delay(POLYLINE_DRAW_TIME)
            }
        }
    }

    // 전체 경로가 다 보이게 줌
    private fun zoomToWholeTrack() {
        val bounds = LatLngBounds.Builder()
        for (polyline in polyLineList) {
            bounds.include(polyline)
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