package com.ssafy.runwithme.view.crew_detail.board

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
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

    override fun init() {
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
            crewDetailViewModel.getPagingCrewBoards(1).collectLatest { pagingData ->
                crewBoardAdapter.submitData(pagingData)
            }
        }
    }
}