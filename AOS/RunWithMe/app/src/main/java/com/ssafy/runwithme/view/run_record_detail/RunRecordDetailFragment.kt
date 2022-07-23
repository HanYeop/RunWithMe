package com.ssafy.runwithme.view.run_record_detail

import androidx.navigation.fragment.findNavController
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentRunRecordDetailBinding

class RunRecordDetailFragment : BaseFragment<FragmentRunRecordDetailBinding>(R.layout.fragment_run_record_detail) {
    override fun init() {
        initClickListener()
    }

    private fun initClickListener() {
        binding.apply {
            btnOk.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
}