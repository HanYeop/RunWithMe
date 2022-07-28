package com.ssafy.runwithme.view.crew_recruit.search.result

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentSearchCrewResultBinding
import com.ssafy.runwithme.model.response.RecruitCrewResponse
import com.ssafy.runwithme.view.crew_recruit.CrewRecruitAdapter
import com.ssafy.runwithme.view.crew_recruit.CrewRecruitListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchCrewResultFragment : BaseFragment<FragmentSearchCrewResultBinding>(R.layout.fragment_search_crew_result) {

    private val args by navArgs<SearchCrewResultFragmentArgs>()
    private val searchCrewResultViewModel by viewModels<SearchCrewResultViewModel>()
    private lateinit var crewRecruitAdapter: CrewRecruitAdapter

    private val CREW_PAGING_SIZE = 10

    private var crewName : String? = ""
    private var startDate : String? = ""
    private var endDate : String? = ""
    private var startTime : String? = ""
    private var endTime : String? = ""
    private var minCost : Int = 0
    private var maxCost : Int = 0
    private var minPurposeAmount : Int = 0
    private var maxPurposeAmount : Int = 0
    private var minGoalDays : Int = 0
    private var maxGoalDays : Int = 7
    private var goalType : String? = ""

    override fun init() {
        crewRecruitAdapter = CrewRecruitAdapter(listener)

        binding.apply {
            recyclerCrewSearchResult.adapter = crewRecruitAdapter
        }

        initNavArgs()

        initClickListener()

        initViewModelCallback()
    }

    private fun initNavArgs() {
        crewName = args.crewname
        startDate = args.startdate + " 00:00:00"
        endDate = args.enddate + "23:59:59"
        startTime = args.starttime + ":00"
        endTime = args.endtime + ":00"
        minCost = args.mincost
        maxCost = args.maxcost
        minPurposeAmount = args.minpurposeamount
        maxPurposeAmount = args.maxpurposeamount
        minGoalDays = args.mingoaldays
        maxGoalDays = args.maxgoaldays
        goalType = args.goaltype

        Log.d("test5", "initNavArgs: $crewName, $startDate, $endDate, $startTime, $endTime, $minCost, $maxCost, $minPurposeAmount, $maxPurposeAmount, $minGoalDays, $maxGoalDays, $goalType")
    }

    private fun initClickListener() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }


    private fun initViewModelCallback() {
        if(crewName == ""){
            crewName = null
        }

        if(startDate == ""){
            startDate = null
        }

        if(endDate == ""){
            endDate = null
        }

        if(startTime == ""){
            startTime = null
        }

        if(endTime == ""){
            endTime = null
        }

        if(goalType == ""){
            goalType = null
        }

        Log.d("test5", "post: $crewName, $startDate, $endDate, $startTime, $endTime, $minCost, $maxCost, $minPurposeAmount, $maxPurposeAmount, $minGoalDays, $maxGoalDays, $goalType")

        lifecycleScope.launch {
            searchCrewResultViewModel.getSearchResultCrew(CREW_PAGING_SIZE, crewName, startDate, endDate,
                startTime,
                endTime,
                minCost,
                maxCost,
                minPurposeAmount,
                maxPurposeAmount,
                minGoalDays,
                maxGoalDays,
                goalType).collectLatest { pagingData ->
                crewRecruitAdapter.submitData(pagingData)
            }
        }
    }

    private val listener : CrewRecruitListener = object : CrewRecruitListener {
        override fun onItemClick(recruitCrewResponse: RecruitCrewResponse) {
            val action = SearchCrewResultFragmentDirections.actionSearchCrewResultFragmentToCrewDetailFragment(recruitCrewResponse.crewDto, recruitCrewResponse.imageFileDto)
            findNavController().navigate(action)
        }
    }
}