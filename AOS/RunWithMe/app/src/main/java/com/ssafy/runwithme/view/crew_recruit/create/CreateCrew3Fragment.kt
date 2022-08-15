package com.ssafy.runwithme.view.crew_recruit.create

import android.annotation.SuppressLint
import android.os.Build
import android.widget.RadioGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentCreateCrew3Binding
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
class CreateCrewFragment3 : BaseFragment<FragmentCreateCrew3Binding>(R.layout.fragment_create_crew3) {

    private val createCrewViewModel by activityViewModels<CreateCrewViewModel>()


    override fun init() {

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

            btnCreateNext.setOnClickListener {
                findNavController().navigate(R.id.action_createCrewFragment3_to_createCrewFragment4)
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
        createCrewViewModel.failMsgEvent.observe(viewLifecycleOwner) {
            showToast(it)
        }
    }

}