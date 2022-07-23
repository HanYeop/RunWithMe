package com.ssafy.runwithme.view.crew_detail.my_record

import androidx.navigation.fragment.findNavController
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentCrewMyRunRecordBinding

class CrewMyRunRecordFragment : BaseFragment<FragmentCrewMyRunRecordBinding>(R.layout.fragment_crew_my_run_record) {
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