package com.ssafy.runwithme.view.competition

import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentCompetitionBinding

class CompetitionFragment : BaseFragment<FragmentCompetitionBinding>(R.layout.fragment_competition) {

    override fun init() {
        showToast("개인 챌린지 시즌1 입니다")
    }

}