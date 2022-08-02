package com.ssafy.runwithme.view.home

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val homeViewModel by activityViewModels<HomeViewModel>()

    override fun init() {
        binding.apply {
            homeVM = homeViewModel
        }

        homeViewModel.getMyProfile()

        initViewModelCallBack()

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

    private fun initViewModelCallBack(){
        homeViewModel.errorMsgEvent.observe(viewLifecycleOwner){
            showToast(it)
        }
    }
}