package com.ssafy.runwithme.view.crew_detail.user_record

import androidx.navigation.fragment.findNavController
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentCrewUserRunRecordBinding

class CrewUserRunRecordFragment : BaseFragment<FragmentCrewUserRunRecordBinding>(R.layout.fragment_crew_user_run_record) {
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