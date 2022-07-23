package com.ssafy.runwithme.view.crew_recruit.search.result

import androidx.navigation.fragment.findNavController
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentSearchCrewResultBinding

class SearchCrewResultFragment : BaseFragment<FragmentSearchCrewResultBinding>(R.layout.fragment_search_crew_result) {
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