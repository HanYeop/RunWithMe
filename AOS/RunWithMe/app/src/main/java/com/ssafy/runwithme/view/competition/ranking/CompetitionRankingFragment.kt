package com.ssafy.runwithme.view.competition.ranking

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentCompetitionRankingBinding
import com.ssafy.runwithme.view.competition.CompetitionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CompetitionRankingFragment : BaseFragment<FragmentCompetitionRankingBinding>(R.layout.fragment_competition_ranking) {

    private val competitionRankingViewModel by viewModels<CompetitionRankingViewModel>()
    private val args by navArgs<CompetitionRankingFragmentArgs>()
    private var userSeq = 0
    private var competitionSeq = 0
    private lateinit var adapter : CompetitionRankingAdapter

    override fun init() {
        adapter = CompetitionRankingAdapter(listener)
        binding.apply {
            recyclerTotalUserRanking.adapter = adapter
            competitionRankingVM = competitionRankingViewModel
        }

        userSeq = args.userseq
        competitionSeq = args.competitionseq

        initClickListener()

        initViewModelCallback()
    }

    private val listener : CompetitionRankingListener = object : CompetitionRankingListener {
        override fun onItemClick(userSeq: Int) {
            val action = CompetitionRankingFragmentDirections.actionCompetitionRankingFragmentToUserDetailFragment(userSeq)
            findNavController().navigate(action)
        }
    }

    private fun initViewModelCallback(){

        competitionRankingViewModel.getCompetitionMyRanking(competitionSeq, userSeq)

        competitionRankingViewModel.getCompetitionTotalRanking(competitionSeq)

        competitionRankingViewModel.errorMsgEvent.observe(viewLifecycleOwner) {
            showToast(it)
        }

    }


    private fun initClickListener(){
        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

}