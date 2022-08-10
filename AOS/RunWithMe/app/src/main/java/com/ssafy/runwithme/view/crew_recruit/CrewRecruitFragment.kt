package com.ssafy.runwithme.view.crew_recruit

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentCrewRecruitBinding
import com.ssafy.runwithme.model.response.RecruitCrewResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CrewRecruitFragment : BaseFragment<FragmentCrewRecruitBinding>(R.layout.fragment_crew_recruit) {

    private val crewRecruitViewModel by viewModels<CrewRecruitViewModel>()
    private lateinit var crewRecruitAdapter : CrewRecruitAdapter

    private val CREW_PAGING_SIZE = 10

    override fun init() {
        crewRecruitAdapter = CrewRecruitAdapter(listener)
        binding.apply {
            recyclerCrewRecruit.adapter = crewRecruitAdapter
        }

        initClickListener()

        initViewModelCallback()
    }

    private fun initClickListener() {
        binding.apply {
            fabCreateCrew.setOnClickListener {
                findNavController().navigate(R.id.action_CrewRecruitFragment_to_createCrewFragment)
            }

            imgBtnSearch.setOnClickListener {
                findNavController().navigate(R.id.action_CrewRecruitFragment_to_searchCrewFragment)
            }

            toolbar.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun initViewModelCallback() {
        lifecycleScope.launch {
            crewRecruitViewModel.getRecruitCrew(CREW_PAGING_SIZE).collectLatest { pagingData ->
                crewRecruitAdapter.submitData(pagingData)
            }
        }
    }

    private val listener : CrewRecruitListener = object : CrewRecruitListener {
        override fun onItemClick(recruitCrewResponse: RecruitCrewResponse) {
            val action = CrewRecruitFragmentDirections.actionCrewRecruitFragmentToCrewDetailFragment(recruitCrewResponse.crewDto, recruitCrewResponse.imageFileDto)
            findNavController().navigate(action)
        }
    }
}