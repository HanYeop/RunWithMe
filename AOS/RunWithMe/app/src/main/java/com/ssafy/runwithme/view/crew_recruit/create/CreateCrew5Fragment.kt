package com.ssafy.runwithme.view.crew_recruit.create

import android.annotation.SuppressLint
import android.os.Build
import android.widget.RadioGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentCreateCrew5Binding
import com.ssafy.runwithme.view.crew_recruit.*
import com.ssafy.runwithme.view.loading.LoadingDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class CreateCrewFragment5 : BaseFragment<FragmentCreateCrew5Binding>(R.layout.fragment_create_crew5) {

    private val createCrewViewModel by activityViewModels<CreateCrewViewModel>()

    private lateinit var loadingDialog: LoadingDialog

    override fun init() {

        loadingDialog = LoadingDialog(requireContext())

        binding.apply {
            crewCreateVM = createCrewViewModel
        }

        initClickListener()

        initViewModelCallBack()

        initRadioGroupCheck()

    }

    @SuppressLint("ResourceAsColor")
    private fun initClickListener() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            btnCreate.setOnClickListener {
                createCrewViewModel.createCrew()
                loading()
            }

            btnCreateGoalAmount.setOnClickListener {
                val check = radioGroupPurpose.checkedRadioButtonId
                if(check == R.id.radio_btn_time){
                    initPurposeTimeDialog()
                }else{
                    initPurposeDistanceDialog()
                }
            }

            btnCreateGoalDays.setOnClickListener {
                initGoalDaysDialog()
            }

        }
    }

    private fun loading(){
        loadingDialog.show()
        // 로딩이 진행되지 않았을 경우
        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
            if(loadingDialog.isShowing){
                loadingDialog.dismiss()
            }
        }
    }

    private fun initRadioGroupCheck() {
        binding.apply {
            radioGroupPurpose.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener{
                override fun onCheckedChanged(radioGroup: RadioGroup?, checkId: Int) {
                    when(checkId){
                        R.id.radio_btn_time -> {
                            tvGoalType.text = "분"
                            createCrewViewModel.setGoalTypeDistance(false)
                        }
                        R.id.radio_btn_distance -> {
                            tvGoalType.text = "km"
                            createCrewViewModel.setGoalTypeDistance(true)
                        }
                    }
                }
            })
        }
    }

    private fun initGoalDaysDialog() {
        val goalDaysDialog = GoalDaysDialog(requireContext(), goalDaysDialogListener, 1)
        goalDaysDialog.show()
    }

    private fun initPurposeDistanceDialog() {
        val purposeDistanceDialog = PurposeDistanceDialog(requireContext(), purposeDistanceDialogListener, 1)
        purposeDistanceDialog.show()
    }

    private fun initPurposeTimeDialog() {
        val purposeTimeDialog = PurposeTimeDialog(requireContext(), purposeTimeDialogListener)
        purposeTimeDialog.show()
    }

    private val goalDaysDialogListener : GoalDaysDialogListener = object : GoalDaysDialogListener {
        override fun onItemClick(days: Int) {
            createCrewViewModel.setGoalDays(days)
        }
    }

    private val purposeTimeDialogListener : PurposeTimeDialogListener = object :
        PurposeTimeDialogListener {
        override fun onItemClick(time: String) {
            createCrewViewModel.setTime(time)
        }
    }

    private val purposeDistanceDialogListener : PurposeDistanceDialogListener = object :
        PurposeDistanceDialogListener {
        override fun onItemClick(distance: Int) {
            createCrewViewModel.setDistance(distance)
        }
    }

    private fun initViewModelCallBack() {
        createCrewViewModel.errorMsgEvent.observe(viewLifecycleOwner) {
            showToast(it)
        }

        createCrewViewModel.successMsgEvent.observe(viewLifecycleOwner) {
            loadingDialog.dismiss()
            showToast(it)
            createCrewViewModel.refresh()
            findNavController().navigate(R.id.action_createCrewFragment5_to_CrewRecruitFragment)
        }

        createCrewViewModel.failMsgEvent.observe(viewLifecycleOwner) {
            showToast(it)
        }
    }

}