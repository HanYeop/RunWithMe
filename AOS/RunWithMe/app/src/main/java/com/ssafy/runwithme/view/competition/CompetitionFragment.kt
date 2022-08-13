package com.ssafy.runwithme.view.competition

import android.content.SharedPreferences
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentCompetitionBinding
import com.ssafy.runwithme.utils.USER
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CompetitionFragment : BaseFragment<FragmentCompetitionBinding>(R.layout.fragment_competition) {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val competitionViewModel by viewModels<CompetitionViewModel>()

    override fun init() {
        binding.apply {
            competitionVM = competitionViewModel
        }
        val userSeq = sharedPreferences.getString(USER, "0")!!.toInt()
        competitionViewModel.userSeq = userSeq

        initClickListener()

        initViewModelCallback()
    }

    private fun initViewModelCallback(){
        competitionViewModel.getInprogressCompetition()

        competitionViewModel.successMsgEvent.observe(viewLifecycleOwner) {
            showToast(it)
            binding.btnJoinCrew.visibility = View.GONE
        }

        competitionViewModel.errorMsgEvent.observe(viewLifecycleOwner) {
            showToast(it)
        }

    }

    private fun initClickListener(){
        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            cardRanking.setOnClickListener {
                val action = CompetitionFragmentDirections.actionCompetitionFragmentToCompetitionRankingFragment(competitionViewModel.userSeq, competitionViewModel.competitionDto.value.competitionSeq)
                findNavController().navigate(action)
            }

            btnJoinCrew.setOnClickListener {
                competitionViewModel.joinCompetition()
            }
        }
    }

}