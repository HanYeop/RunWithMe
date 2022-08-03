package com.ssafy.runwithme.view.crew_detail.ranking

import CrewUserRankingAdapter
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentCrewUserRankingBinding
import com.ssafy.runwithme.model.dto.CrewDto
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CrewUserRankingFragment : BaseFragment<FragmentCrewUserRankingBinding>(R.layout.fragment_crew_user_ranking) {

    private val args by navArgs<CrewUserRankingFragmentArgs>()
    private lateinit var crewDto: CrewDto
    private val crewUserRankingViewModel by viewModels<CrewUserRankingViewModel>()
    private lateinit var adapter : CrewUserRankingAdapter

    override fun init() {
        adapter = CrewUserRankingAdapter(crewUserRankingViewModel)
        binding.apply {
            crewUserRankingVM = crewUserRankingViewModel

            recyclerCrewRanking.adapter = adapter
            spinnerCrewRanking.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) { // 1. 거리 , 2. 시간, 3. 포인트
                    var type = when(position) {
                        0 -> "distance"
                        1 -> "time"
                        else -> ""
                    }
                    crewUserRankingViewModel.getCrewRanking(crewDto.crewSeq, type)
                    adapter.notifyDataSetChanged()
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }
        crewDto = args.crewdto

        initClickListener()

        //initSpinner()
    }

    private fun initSpinner(){

    }

    private fun initClickListener() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
}