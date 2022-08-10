package com.ssafy.runwithme.view.home.tab.crew

import androidx.navigation.fragment.findNavController
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentTabCrewBinding

class TabCrewFragment : BaseFragment<FragmentTabCrewBinding>(R.layout.fragment_tab_crew) {

    override fun init() {
        initClickListener()
    }

    private fun initClickListener(){
        binding.apply {
            cardShowMyCrew.setOnClickListener {
                findNavController().navigate(R.id.action_HomeFragment_to_myCurrentCrewFragment)
            }
            cardShowJoinCrew.setOnClickListener {
                findNavController().navigate(R.id.action_HomeFragment_to_CrewRecruitFragment)
            }
        }
    }

}