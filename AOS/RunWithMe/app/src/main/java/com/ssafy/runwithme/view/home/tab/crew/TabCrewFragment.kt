package com.ssafy.runwithme.view.home.tab.crew

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentTabCrewBinding
import com.ssafy.runwithme.model.response.RecruitCrewResponse
import com.ssafy.runwithme.view.crew_recruit.CrewRecruitListener
import com.ssafy.runwithme.view.crew_recruit.CrewRecruitViewModel
import com.ssafy.runwithme.view.home.HomeFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TabCrewFragment : BaseFragment<FragmentTabCrewBinding>(R.layout.fragment_tab_crew) {

    private val crewRecruitViewModel by viewModels<CrewRecruitViewModel>()

    override fun init() {
        crewRecruitViewModel.getRecruitCrewPreView(6)

        binding.apply {
            crewRecruitVM = crewRecruitViewModel
            recyclerCrewRecruitPreview.adapter = CrewRecruitPreviewAdapter(listener)
        }

        initClickListener()

        initViewModelCallback()
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

    private fun initViewModelCallback(){
        crewRecruitViewModel.errorMsgEvent.observe(viewLifecycleOwner){
            showToast(it)
        }
    }

    private val listener : CrewRecruitListener = object : CrewRecruitListener {
        override fun onItemClick(recruitCrewResponse: RecruitCrewResponse) {
            val action = HomeFragmentDirections.actionHomeFragmentToCrewRecruitDetailFragment(recruitCrewResponse.crewDto, recruitCrewResponse.imageFileDto)
            findNavController().navigate(action)
        }
    }

}