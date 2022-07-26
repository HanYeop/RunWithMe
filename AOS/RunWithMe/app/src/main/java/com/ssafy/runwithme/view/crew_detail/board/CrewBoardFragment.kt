package com.ssafy.runwithme.view.crew_detail.board

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentCrewBoardBinding
import com.ssafy.runwithme.view.crew_detail.CrewDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CrewBoardFragment : BaseFragment<FragmentCrewBoardBinding>(R.layout.fragment_crew_board) {

    private val crewDetailViewModel by viewModels<CrewDetailViewModel>()
    private val crewBoardAdapter = CrewBoardAdapter()
    private val args by navArgs<CrewBoardFragmentArgs>()

    override fun init() {
        binding.apply {
            recyclerCrewBoard.adapter = crewBoardAdapter
        }

        initClickListener()

        initViewModelCallback()
    }

    private fun initClickListener() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            fabCreateBoard.setOnClickListener {
                findNavController().navigate(R.id.action_crewBoardFragment_to_createCrewBoardFragment)
            }
        }
    }

    private fun initViewModelCallback(){
        lifecycleScope.launch {
            crewDetailViewModel.getCrewBoards(args.crewid,10).collectLatest { pagingData ->
                crewBoardAdapter.submitData(pagingData)
            }
        }
    }
}