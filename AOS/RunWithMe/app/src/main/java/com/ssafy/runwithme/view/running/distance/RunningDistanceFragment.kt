package com.ssafy.runwithme.view.running.distance

import android.content.SharedPreferences
import android.location.Location
import android.util.Log
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentRunningDistanceBinding
import com.ssafy.runwithme.service.Polyline
import com.ssafy.runwithme.service.RunningService
import com.ssafy.runwithme.utils.TrackingUtility
import com.ssafy.runwithme.view.running.RunningFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RunningDistanceFragment : BaseFragment<FragmentRunningDistanceBinding>(R.layout.fragment_running_distance) {

    // 라이브 데이터를 받아온 값들
    private var pathPoints = mutableListOf<Polyline>()
    private var currentTimeInMillis = 0L

    // SharedPreferences 주입
    @Inject
    lateinit var sharedPref: SharedPreferences

    // 총 이동거리
    private var sumDistance = 0f

    override fun init() {
        sumDistance = updateDistance()
        binding.tvCurrentTime.text = "${TrackingUtility.getFormattedDistance(sumDistance)}Km"

        initLiveData()
    }

    private fun initLiveData(){
        // 경로 변경 관찰
        RunningService.pathPoints.observe(this) {
            pathPoints = it
            addLatestDistance()

            // 거리 텍스트 변경
            binding.tvDistance.text = "${TrackingUtility.getFormattedDistance(sumDistance)}Km"
            Log.d("test5", "distance: ${TrackingUtility.getFormattedDistance(sumDistance)}")
        }

        // 시간(타이머) 경과 관찰
        RunningService.timeRunInMillis.observe(this) {
            currentTimeInMillis = it
            val formattedTime = TrackingUtility.getFormattedStopWatchTime(it, true)
            binding.tvCurrentTime.text = formattedTime
        }
    }

    // 경로 표시 (마지막 전, 마지막 경로 연결)
    private fun addLatestDistance(){
        if(pathPoints.isNotEmpty() && pathPoints.last().size > 1){
            val preLastLatLng = pathPoints.last()[pathPoints.last().size - 2] // 마지막 전 경로
            val lastLatLng = pathPoints.last().last() // 마지막 경로

            // 이동거리 계산
            val result = FloatArray(1)
            Location.distanceBetween(
                preLastLatLng.latitude,
                preLastLatLng.longitude,
                lastLatLng.latitude,
                lastLatLng.longitude,
                result
            )
            sumDistance += result[0]
        }
    }

    // 총 이동거리
    private fun updateDistance() : Float{
        var distanceInMeters = 0f
        for (polyline in pathPoints) {
            distanceInMeters += calculatePolylineLength(polyline)
        }
        return distanceInMeters
    }

    // 총 이동거리 계산
    private fun calculatePolylineLength(polyline: Polyline): Float {
        var distance = 0f
        // 두 경로 사이마다 거리를 계산하여 합함
        for (i in 0 until polyline.size - 1) {
            val pos1 = polyline[i]
            val pos2 = polyline[i + 1]
            val result = FloatArray(1)
            Location.distanceBetween(
                pos1.latitude,
                pos1.longitude,
                pos2.latitude,
                pos2.longitude,
                result
            )
            distance += result[0]
        }
        return distance
    }
}