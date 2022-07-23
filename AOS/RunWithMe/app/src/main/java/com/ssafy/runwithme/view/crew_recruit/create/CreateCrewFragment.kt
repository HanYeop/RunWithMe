package com.ssafy.runwithme.view.crew_recruit.create

import androidx.navigation.fragment.findNavController
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentCreateCrewBinding

class CreateCrewFragment : BaseFragment<FragmentCreateCrewBinding>(R.layout.fragment_create_crew) {
    override fun init() {
        initClickListener()
    }

    private fun initClickListener() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            // 비즈니스 로직 구현하기
            btnCreate.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
}