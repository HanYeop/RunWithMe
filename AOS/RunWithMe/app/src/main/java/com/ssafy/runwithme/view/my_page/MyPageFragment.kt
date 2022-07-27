package com.ssafy.runwithme.view.my_page

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentMyPageBinding
import com.ssafy.runwithme.view.my_page.tab.achievement.AchievementFragment
import com.ssafy.runwithme.view.my_page.tab.my_board.MyBoardFragment
import com.ssafy.runwithme.view.my_page.tab.total_record.MyTotalRunRecordFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    private lateinit var myBoardFragment: MyBoardFragment
    private lateinit var myTotalRunRecordFragment: MyTotalRunRecordFragment
    private lateinit var achievementFragment: AchievementFragment
    private val myPageViewModel by viewModels<MyPageViewModel>()

    override fun init() {
        initTabLayout()
        initClickListener()
        initViewModelCallBack()

        myPageViewModel.getMyProfile()
        binding.myPageVM = myPageViewModel
    }

    private fun initClickListener() {
        binding.apply {
            imageSettings.setOnClickListener {
                findNavController().navigate(R.id.action_MyPageFragment_to_othersFragment)
            }
            imageEditProfile.setOnClickListener {
                findNavController().navigate(R.id.action_MyPageFragment_to_editProfileFragment)
            }
        }
    }

    private fun initViewModelCallBack(){
        myPageViewModel.errorMsgEvent.observe(viewLifecycleOwner){
            showToast(it)
        }
    }

    // 각 탭에 들어갈 프레그먼트 객체화
    private fun initTabLayout(){
        myTotalRunRecordFragment = MyTotalRunRecordFragment()
        myBoardFragment = MyBoardFragment()
        achievementFragment = AchievementFragment()

        childFragmentManager.beginTransaction().replace(R.id.frame_layout_my_page, myTotalRunRecordFragment).commit()

        binding.tabLayoutMyPage.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab!!.position) {
                    0 -> replaceView(myTotalRunRecordFragment)
                    1 -> replaceView(myBoardFragment)
                    2 -> replaceView(achievementFragment)
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
            childFragmentManager.beginTransaction().replace(R.id.frame_layout_my_page, it).commit()
        }
    }

}