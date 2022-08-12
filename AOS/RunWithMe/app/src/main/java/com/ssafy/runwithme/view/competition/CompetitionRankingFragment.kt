package com.ssafy.runwithme.view.competition

import androidx.navigation.fragment.findNavController
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentCompetitionRankingBinding

class CompetitionRankingFragment : BaseFragment<FragmentCompetitionRankingBinding>(R.layout.fragment_competition_ranking) {

    override fun init() {
        initClickListener()
    }


    private fun initClickListener(){
        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

}