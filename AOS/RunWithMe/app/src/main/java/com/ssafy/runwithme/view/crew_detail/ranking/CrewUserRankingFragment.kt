package com.ssafy.runwithme.view.crew_detail.ranking

import androidx.navigation.fragment.findNavController
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentCrewUserRankingBinding

class CrewUserRankingFragment : BaseFragment<FragmentCrewUserRankingBinding>(R.layout.fragment_crew_user_ranking) {
    override fun init() {
        initClickListener()
    }

    private fun initClickListener() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
}