package com.ssafy.runwithme.view.crew_detail.board

import androidx.navigation.fragment.findNavController
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentCrewBoardBinding

class CrewBoardFragment : BaseFragment<FragmentCrewBoardBinding>(R.layout.fragment_crew_board) {
    override fun init() {
        initClickListener()
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

}