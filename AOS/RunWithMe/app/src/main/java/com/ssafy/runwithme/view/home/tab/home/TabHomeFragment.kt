package com.ssafy.runwithme.view.home.tab.home

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentTabHomeBinding
import com.ssafy.runwithme.view.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TabHomeFragment : BaseFragment<FragmentTabHomeBinding>(R.layout.fragment_tab_home) {

    private val homeViewModel by activityViewModels<HomeViewModel>()

    override fun init() {
        binding.homeVM = homeViewModel

        initClickListener()
    }

    private fun initClickListener(){
        binding.apply {
            cardShowTotalRank.setOnClickListener {
                findNavController().navigate(R.id.action_HomeFragment_to_totalUserRankingFragment)
            }
            cardShowRecommend.setOnClickListener {
                findNavController().navigate(R.id.action_HomeFragment_to_RecommendFragment)
            }
            cardShowCompetition.setOnClickListener {
                findNavController().navigate(R.id.action_HomeFragment_to_competitionFragment)
            }
        }
    }

}