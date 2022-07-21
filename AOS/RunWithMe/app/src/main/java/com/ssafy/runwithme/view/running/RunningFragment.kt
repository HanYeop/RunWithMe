package com.ssafy.runwithme.view.running

import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentRunningBinding
import com.ssafy.runwithme.view.running.distance.RunningDistanceFragment
import com.ssafy.runwithme.view.running.map.RunningMapFragment

class RunningFragment : BaseFragment<FragmentRunningBinding>(R.layout.fragment_running) {

    private lateinit var runningDistanceFragment: RunningDistanceFragment
//    private lateinit var runningTimeFragment: RunningTimeFragment
    private lateinit var runningMapFragment: RunningMapFragment

    override fun init() {
        initTabLayout()
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
}