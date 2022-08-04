package com.ssafy.runwithme.view.running.list.sheet

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ssafy.runwithme.R
import com.ssafy.runwithme.databinding.DialogRunningBottomSheetBinding
import com.ssafy.runwithme.model.response.MyCurrentCrewResponse
import com.ssafy.runwithme.utils.GOAL_TYPE_TIME
import com.ssafy.runwithme.utils.runningStart
import com.ssafy.runwithme.view.running.RunningActivity
import com.ssafy.runwithme.view.running.RunningViewModel
import com.ssafy.runwithme.view.running.list.RunningListAdapter
import com.ssafy.runwithme.view.running.list.RunningListListener
import com.ssafy.runwithme.view.running.list.sheet.custom.PracticeCustomDialog
import com.ssafy.runwithme.view.running.list.sheet.custom.PracticeCustomListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RunningBottomSheet(context: Context, private val sharedPreferences: SharedPreferences): BottomSheetDialogFragment() {

    private var _binding: DialogRunningBottomSheetBinding? = null
    val binding get() = _binding!!

    private val runningViewModel by viewModels<RunningViewModel>()
    private lateinit var runningListAdapter: RunningListAdapter
    private lateinit var myCurrentInfo : MyCurrentCrewResponse

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.dialog_running_bottom_sheet, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        init()
    }

    private fun init(){
        runningListAdapter = RunningListAdapter(listener)

        binding.apply {
            runningVM = runningViewModel
            recyclerRunningList.adapter = runningListAdapter
        }

        initClickListener()

        initViewModelCallBack()

        runningViewModel.getMyCurrentCrew()
    }

    private fun initViewModelCallBack(){
        runningViewModel.errorMsgEvent.observe(this){
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        runningViewModel.startRunEvent.observe(this){
            runningStart(sharedPreferences, myCurrentInfo.crewDto.crewSeq, myCurrentInfo.crewDto.crewName
                ,myCurrentInfo.crewDto.crewGoalType, myCurrentInfo.crewDto.crewGoalAmount)
            startActivity(Intent(requireContext(), RunningActivity::class.java))
            dismiss()
        }

        lifecycleScope.launch {
            runningViewModel.runningCrewList.collectLatest {
                runningListAdapter.submitList(it)
            }
        }
    }

    val listener = object : RunningListListener {
        override fun onItemClick(myCurrentCrewResponse: MyCurrentCrewResponse) {
            myCurrentInfo = myCurrentCrewResponse
            runningViewModel.getMyProfile()
        }
    }

    private fun initClickListener(){
        binding.apply {
            layoutCardCrew.setOnClickListener {
                PracticeCustomDialog(requireContext(), practiceListener).show()
            }
        }
    }

    private val practiceListener = object : PracticeCustomListener{
        override fun onItemClick(type: String, amount: Int) {
            runningStart(sharedPreferences, 0, "연습크루", type, amount)
            startActivity(Intent(context, RunningActivity::class.java))
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}