package com.ssafy.runwithme.view.crew_recruit.search

import androidx.navigation.fragment.findNavController
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentSearchCrewBinding

class SearchCrewFragment : BaseFragment<FragmentSearchCrewBinding>(R.layout.fragment_search_crew) {
    override fun init() {
        initClickListener()
    }

    private fun initClickListener() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            // 비즈니스 로직 구현하기
            btnSearch.setOnClickListener {
                findNavController().navigate(R.id.action_searchCrewFragment_to_searchCrewResultFragment)
            }
        }
    }
}