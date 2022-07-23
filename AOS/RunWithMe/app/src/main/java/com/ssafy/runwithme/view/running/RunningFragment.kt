package com.ssafy.runwithme.view.running

import android.content.Intent
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
    }

    private fun firstStart(){
        if(!RunningService.isFirstRun){
            runningLoadingDialog = RunningLoadingDialog(requireContext())
            sendCommandToService(ACTION_SHOW_RUNNING_ACTIVITY)
            runningLoadingDialog.show()
            CoroutineScope(Dispatchers.Main).launch {
                delay(3500L)
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