package com.ssafy.runwithme.view.crew_detail

import androidx.navigation.fragment.findNavController
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentCrewDetailBinding

class CrewDetailFragment : BaseFragment<FragmentCrewDetailBinding>(R.layout.fragment_crew_detail) {
    override fun init() {
        initClickListener()
    }

    private fun initClickListener(){
        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            tvMyRecordMore.setOnClickListener {
                findNavController().navigate(R.id.action_crewDetailFragment_to_crewMyRunRecordFragment)
            }

            tvRankingMore.setOnClickListener {
                findNavController().navigate(R.id.action_crewDetailFragment_to_crewUserRankingFragment)
            }

            tvBoardMore.setOnClickListener {
                findNavController().navigate(R.id.action_crewDetailFragment_to_crewBoardFragment)
            }

            tvRankingMore.setOnClickListener {
                findNavController().navigate(R.id.action_crewDetailFragment_to_crewUserRankingFragment)
            }
        }
    }
}