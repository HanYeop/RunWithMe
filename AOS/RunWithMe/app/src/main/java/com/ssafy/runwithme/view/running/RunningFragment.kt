package com.ssafy.runwithme.view.running

import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentRunningBinding
import com.ssafy.runwithme.service.RunningService
import com.ssafy.runwithme.utils.ACTION_PAUSE_SERVICE
import com.ssafy.runwithme.utils.ACTION_SHOW_RUNNING_ACTIVITY
import com.ssafy.runwithme.utils.ACTION_START_OR_RESUME_SERVICE
import com.ssafy.runwithme.utils.ACTION_STOP_SERVICE
import com.ssafy.runwithme.view.running.distance.RunningDistanceFragment
import com.ssafy.runwithme.view.running.map.RunningMapFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RunningFragment : BaseFragment<FragmentRunningBinding>(R.layout.fragment_running) {

    // 라이브 데이터를 받아온 값들
    private var isTracking = false

    private lateinit var runningDistanceFragment: RunningDistanceFragment
//    private lateinit var runningTimeFragment: RunningTimeFragment
    private lateinit var runningMapFragment: RunningMapFragment

    private lateinit var runningLoadingDialog: RunningLoadingDialog

    override fun init() {
        initTabLayout()

        initClickListener()

        firstStart()

        // 위치 추적 여부 관찰하여 updateTracking 호출
        RunningService.isTracking.observe(this){
            updateTracking(it)
        }
    }

    private fun firstStart(){
        if(!RunningService.isFirstRun){
            runningLoadingDialog = RunningLoadingDialog(requireContext())
            runningLoadingDialog.show()
            CoroutineScope(Dispatchers.Main).launch {
                delay(3000L)
                sendCommandToService(ACTION_SHOW_RUNNING_ACTIVITY)
                delay(500L)
                runningLoadingDialog.dismiss()
                sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
            }
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

    // 위치 추적 상태에 따른 레이아웃 변경
    private fun updateTracking(isTracking: Boolean) {
        this.isTracking = isTracking
        binding.apply {
            if (!isTracking) {
                imgPause.visibility = View.INVISIBLE
                imgStart.visibility = View.VISIBLE
                imgStop.visibility = View.VISIBLE
            }
            else if (isTracking) {
                imgPause.visibility = View.VISIBLE
                imgStart.visibility = View.GONE
                imgStop.visibility = View.GONE
            }
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
            }
            imgStart.setOnClickListener {
                sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
            }
            imgStop.setOnClickListener {
                stopRun()
            }
        }
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