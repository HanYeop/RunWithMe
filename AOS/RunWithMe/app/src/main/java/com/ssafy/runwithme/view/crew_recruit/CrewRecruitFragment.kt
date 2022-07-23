package com.ssafy.runwithme.view.crew_recruit

import androidx.navigation.fragment.findNavController
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentCrewRecruitBinding

class CrewRecruitFragment : BaseFragment<FragmentCrewRecruitBinding>(R.layout.fragment_crew_recruit) {
    override fun init() {
        initClickListener()
    }

    private fun initClickListener() {
        binding.apply {
            fabCreateCrew.setOnClickListener {
                findNavController().navigate(R.id.action_CrewRecruitFragment_to_createCrewFragment)
            }

            imgBtnSearch.setOnClickListener {
                findNavController().navigate(R.id.action_CrewRecruitFragment_to_searchCrewFragment)
            }
        }
    }
}