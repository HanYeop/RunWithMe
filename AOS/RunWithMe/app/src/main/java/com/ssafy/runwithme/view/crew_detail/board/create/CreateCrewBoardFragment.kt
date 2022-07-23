package com.ssafy.runwithme.view.crew_detail.board.create

import androidx.navigation.fragment.findNavController
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentCreateCrewBoardBinding

class CreateCrewBoardFragment : BaseFragment<FragmentCreateCrewBoardBinding>(R.layout.fragment_create_crew_board) {
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