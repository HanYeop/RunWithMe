package com.ssafy.runwithme.view.running.list.sheet

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ssafy.runwithme.R
import com.ssafy.runwithme.databinding.DialogRunningBottomSheetBinding
import com.ssafy.runwithme.model.response.MyCurrentCrewResponse
import com.ssafy.runwithme.utils.runningStart
import com.ssafy.runwithme.view.running.RunningActivity
import com.ssafy.runwithme.view.running.RunningViewModel
import com.ssafy.runwithme.view.running.list.RunningListAdapter
import com.ssafy.runwithme.view.running.list.RunningListListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RunningBottomSheet(context: Context, private val sharedPreferences: SharedPreferences): BottomSheetDialogFragment() {

    private var _binding: DialogRunningBottomSheetBinding? = null
    val binding get() = _binding!!

    private val runningViewModel by viewModels<RunningViewModel>()
    private lateinit var runningListAdapter: RunningListAdapter

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
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    val listener = object : RunningListListener {
        override fun onItemClick(myCurrentCrewResponse: MyCurrentCrewResponse) {
            runningViewModel.getMyProfile()
            runningStart(sharedPreferences, myCurrentCrewResponse.crewDto.crewSeq, myCurrentCrewResponse.crewDto.crewName
                ,myCurrentCrewResponse.crewDto.crewGoalType, myCurrentCrewResponse.crewDto.crewGoalAmount)
            startActivity(Intent(context, RunningActivity::class.java))
            dismiss()
        }
    }

    private fun initClickListener(){
        binding.apply {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}