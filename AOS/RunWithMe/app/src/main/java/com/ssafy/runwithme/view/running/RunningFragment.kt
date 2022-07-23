package com.ssafy.runwithme.view.running

import android.content.Intent
import android.content.SharedPreferences
import android.location.Location
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentRunningBinding
import com.ssafy.runwithme.service.Polyline
import com.ssafy.runwithme.service.RunningService
import com.ssafy.runwithme.utils.ACTION_PAUSE_SERVICE
import com.ssafy.runwithme.utils.ACTION_START_OR_RESUME_SERVICE
import com.ssafy.runwithme.utils.ACTION_STOP_SERVICE
import com.ssafy.runwithme.utils.TrackingUtility
import com.ssafy.runwithme.view.running.distance.RunningDistanceFragment
import com.ssafy.runwithme.view.running.map.RunningMapFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RunningFragment : BaseFragment<FragmentRunningBinding>(R.layout.fragment_running) {

    private lateinit var runningDistanceFragment: RunningDistanceFragment
//    private lateinit var runningTimeFragment: RunningTimeFragment
    private lateinit var runningMapFragment: RunningMapFragment

    // 라이브 데이터를 받아온 값들
    private var isTracking = false
    private var pathPoints = mutableListOf<Polyline>()
    private var currentTimeInMillis = 0L

    // SharedPreferences 주입
    @Inject
    lateinit var sharedPref: SharedPreferences

    // 총 이동거리
    private var sumDistance = 0f

    companion object{
        var distanceText = "0Km"
        var timeText = "00 : 00"
    }

    override fun init() {
        initTabLayout()

        initClickListener()

        sumDistance = updateDistance()
        distanceText = "${TrackingUtility.getFormattedDistance(sumDistance)}Km"

        initLiveData()
    }

    private fun initLiveData(){
        // 경로 변경 관찰
        RunningService.pathPoints.observe(this) {
            pathPoints = it
//            addLatestPolyline()
//            moveCameraToUser()

            // 거리 텍스트 변경
            distanceText = "${TrackingUtility.getFormattedDistance(sumDistance)}Km"

            Log.d("test5", "distance: ${TrackingUtility.getFormattedDistance(sumDistance)}")
        }

        // 시간(타이머) 경과 관찰
        RunningService.timeRunInMillis.observe(this) {
            currentTimeInMillis = it
            val formattedTime = TrackingUtility.getFormattedStopWatchTime(it, true)
            timeText = formattedTime

            Log.d("test5", "time: ${TrackingUtility.getFormattedStopWatchTime(it, true)}")
        }
    }

    // 각 탭에 들어갈 프레그먼트 객체화
    private fun initTabLayout(){
        runningDistanceFragment = RunningDistanceFragment()
//        runningTimeFragment = RunningTimeFragment()
        runningMapFragment = RunningMapFragment()

        childFragmentManager.beginTransaction().replace(R.id.frame_layout, runningDistanceFragment).commit()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab!!.position) {
                    0 -> replaceView(runningDistanceFragment)
                    1 -> replaceView(runningMapFragment)
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    // 화면 변경
    private fun replaceView(tab : Fragment){
        var selectedFragment = tab
        selectedFragment.let {
            childFragmentManager.beginTransaction().replace(R.id.frame_layout, it).commit()
        }
    }

    private fun initClickListener(){
        // 스타트 버튼 클릭 시 서비스를 시작함
        binding.apply {
            imgPause.setOnClickListener {
                // 이미 실행 중이면 일시 정지
                if(isTracking) {
                    sendCommandToService(ACTION_PAUSE_SERVICE)
                }
                // 아니라면 실행
                else{
                    sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
                }
            }
            imgStop.setOnClickListener {
                stopRun()
            }
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

    // 서비스에게 명령을 전달함
    private fun sendCommandToService(action : String) =
        Intent(requireContext(),RunningService::class.java).also {
            it.action = action
            requireActivity().startService(it)
        }

    // 달리기 종료
    private fun stopRun() {
        sendCommandToService(ACTION_STOP_SERVICE)
//        finish()
    }
}