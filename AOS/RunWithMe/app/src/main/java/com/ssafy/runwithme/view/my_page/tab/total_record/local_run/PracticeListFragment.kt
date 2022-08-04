package com.ssafy.runwithme.view.my_page.tab.total_record.local_run

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentPracticeListBinding
import com.ssafy.runwithme.view.running.RunningViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PracticeListFragment : BaseFragment<FragmentPracticeListBinding>(R.layout.fragment_practice_list) {

    private val runningViewModel by viewModels<RunningViewModel>()
    private val practiceListAdapter = PracticeListAdapter()

    override fun init() {
        binding.recyclerPracticeList.adapter = practiceListAdapter

        runningViewModel.getAllRunsSortedByDate()

        initViewModelCallBack()

        initClickListener()
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