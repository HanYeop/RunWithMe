package com.ssafy.runwithme.view.recommend

import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentRecommendBinding
import com.ssafy.runwithme.view.my_page.tab.achievement.AchievementFragment
import com.ssafy.runwithme.view.my_page.tab.my_board.MyBoardFragment
import com.ssafy.runwithme.view.my_page.tab.total_record.MyTotalRunRecordFragment
import com.ssafy.runwithme.view.recommend.admin.AdminRecommendFragment
import com.ssafy.runwithme.view.recommend.user.UserRecommendFragment

class RecommendFragment : BaseFragment<FragmentRecommendBinding>(R.layout.fragment_recommend) {

    private lateinit var adminRecommendFragment: AdminRecommendFragment
    private lateinit var userRecommendFragment: UserRecommendFragment

    override fun init() {
        initTabLayout()
    }

    // 각 탭에 들어갈 프레그먼트 객체화
    private fun initTabLayout(){
        adminRecommendFragment = AdminRecommendFragment()
        userRecommendFragment = UserRecommendFragment()

        childFragmentManager.beginTransaction().replace(R.id.frame_layout, adminRecommendFragment).commit()

        binding.tabLayoutRecommend.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab!!.position) {
                    0 -> replaceView(adminRecommendFragment)
                    1 -> replaceView(userRecommendFragment)
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