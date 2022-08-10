package com.ssafy.runwithme.view.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentHomeBinding
import com.ssafy.runwithme.view.home.tab.home.TabHomeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val homeViewModel by activityViewModels<HomeViewModel>()

    private lateinit var tabHomeFragment : TabHomeFragment

    override fun init() {
        binding.apply {
            homeVM = homeViewModel
        }

        homeViewModel.getMyProfile()

        initTabLayout()

        initViewModelCallBack()

    }

    // 각 탭에 들어갈 프레그먼트 객체화
    private fun initTabLayout(){
        tabHomeFragment = TabHomeFragment()
        replaceView(tabHomeFragment)

        binding.tabLayoutHome.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab!!.position) {
                    0 -> replaceView(tabHomeFragment)
                    // 1 -> replaceView()
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
            childFragmentManager.beginTransaction().replace(R.id.frame_layout_home, it).commit()
        }
    }

    private fun initViewModelCallBack(){
        homeViewModel.errorMsgEvent.observe(viewLifecycleOwner){
            showToast(it)
        }
    }
}