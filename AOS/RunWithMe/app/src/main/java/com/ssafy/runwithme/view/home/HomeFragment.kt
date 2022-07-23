package com.ssafy.runwithme.view.home

import android.content.Intent
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentHomeBinding
import com.ssafy.runwithme.view.running.RunningActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val homeViewModel by viewModels<HomeViewModel>()
    private val homeMyCurrentCrewAdapter = HomeMyCurrentCrewAdapter()

    override fun init() {
        binding.apply {
            homeVM = homeViewModel
            recyclerMyCrewHorizon.adapter = homeMyCurrentCrewAdapter
        }

        homeViewModel.getMyCurrentCrew()
        initClickListener()
    }

    private fun initClickListener(){
        binding.apply {
            // TEST
            tvMyCurrentCrew.setOnClickListener {
                startActivity(Intent(requireContext(),RunningActivity::class.java))
            }
            tvCrewMore.setOnClickListener {
                findNavController().navigate(R.id.action_HomeFragment_to_myCurrentCrewFragment)
            }
            tvRankingMore.setOnClickListener {
                findNavController().navigate(R.id.action_HomeFragment_to_totalUserRankingFragment)
            }

        }
    }
}