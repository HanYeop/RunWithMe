package com.ssafy.runwithme.view.crew_detail.user_record

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentCrewUserRunRecordBinding
import com.ssafy.runwithme.model.dto.RunRecordDto
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CrewUserRunRecordFragment : BaseFragment<FragmentCrewUserRunRecordBinding>(R.layout.fragment_crew_user_run_record) {

    private val crewUserRunRecordViewModel by viewModels<CrewUserRunRecordViewModel>()
    private lateinit var crewUserRunRecordAdapter: CrewUserRunRecordAdapter
    private val args by navArgs<CrewUserRunRecordFragmentArgs>()
    private var crewSeq: Int = 0

    override fun init() {
        crewUserRunRecordAdapter = CrewUserRunRecordAdapter(listener)
        binding.apply {
            recyclerUserRecord.adapter = crewUserRunRecordAdapter
        }

        crewSeq = args.crewid

        initClickListener()

        initViewModelCallback()
    }

    private fun initClickListener() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun initViewModelCallback() {
        lifecycleScope.launch {
            crewUserRunRecordViewModel.getCrewRecords(crewSeq, 10).collectLatest { pagingData ->
                crewUserRunRecordAdapter.submitData(pagingData)
            }
        }
    }

    private val listener : CrewUserRunRecordListener = object : CrewUserRunRecordListener{
        override fun onItemClick(runRecord: RunRecordDto) {
            val action = CrewUserRunRecordFragmentDirections.actionCrewUserRunRecordFragmentToRunRecordDetailFragment(runRecord)
            findNavController().navigate(action)
        }
    }

}