package com.ssafy.runwithme.view.home.tab.home

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentTabHomeBinding
import com.ssafy.runwithme.view.home.HomeViewModel

class TabHomeFragment : BaseFragment<FragmentTabHomeBinding>(R.layout.fragment_tab_home) {

    private val homeViewModel by activityViewModels<HomeViewModel>()

    override fun init() {
        binding.homeVM = homeViewModel

        initClickListener()
    }

    private fun initClickListener(){
        binding.apply {
            cardShowJoinCrew.setOnClickListener {
                findNavController().navigate(R.id.action_HomeFragment_to_CrewRecruitFragment)
            }
            layoutMyCrew.setOnClickListener {
                findNavController().navigate(R.id.action_HomeFragment_to_myCurrentCrewFragment)
            }
            cardShowTotalRank.setOnClickListener {
                findNavController().navigate(R.id.action_HomeFragment_to_totalUserRankingFragment)
            }
            cardShowRecommend.setOnClickListener {
                findNavController().navigate(R.id.action_HomeFragment_to_RecommendFragment)
            }
        }
    }

}