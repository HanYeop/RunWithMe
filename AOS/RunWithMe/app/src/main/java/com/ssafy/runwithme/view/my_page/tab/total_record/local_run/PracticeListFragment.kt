package com.ssafy.runwithme.view.my_page.tab.total_record.local_run

import android.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentPracticeListBinding
import com.ssafy.runwithme.model.entity.RunRecordEntity
import com.ssafy.runwithme.view.running.RunningViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PracticeListFragment : BaseFragment<FragmentPracticeListBinding>(R.layout.fragment_practice_list) {

    private val runningViewModel by viewModels<RunningViewModel>()
    private lateinit var practiceListAdapter: PracticeListAdapter

    override fun init() {
        practiceListAdapter = PracticeListAdapter(listener)
        binding.recyclerPracticeList.adapter = practiceListAdapter

        binding.apply {
            runningVm = runningViewModel
        }

        runningViewModel.getAllRunsSortedByDate()

        runningViewModel.getTotalTimeInMillis()

        runningViewModel.getTotalDistance()

        runningViewModel.getTotalAvgSpeed()

        runningViewModel.getTotalCaloriesBurned()

        initViewModelCallBack()

        initClickListener()
    }

    private val listener = object : PracticeListListener{
        override fun onDeleteClick(run: RunRecordEntity) {
            AlertDialog.Builder(requireContext())
                .setTitle("연습 기록을 삭제하시겠습니까?")
                .setPositiveButton("확인") { _, _ ->
                    runningViewModel.deleteRun(run)
                }
                .setNegativeButton("취소") { _, _ -> }
                .show()
        }
    }

    private fun initClickListener(){
        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun initViewModelCallBack(){
        lifecycleScope.launch {
            runningViewModel.localRunList.collectLatest {
                practiceListAdapter.submitList(it)
            }
        }
    }
}