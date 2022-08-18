package com.ssafy.runwithme.view.crew_recruit.create

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentCreateCrew2Binding
import com.ssafy.runwithme.view.crew_recruit.EndTimeDialog
import com.ssafy.runwithme.view.crew_recruit.EndTimeDialogListener
import com.ssafy.runwithme.view.crew_recruit.StartTimeDialog
import com.ssafy.runwithme.view.crew_recruit.StartTimeDialogListener
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class CreateCrewFragment2 : BaseFragment<FragmentCreateCrew2Binding>(R.layout.fragment_create_crew2) {

    private val createCrewViewModel by activityViewModels<CreateCrewViewModel>()

    override fun init() {

        binding.apply {
            crewCreateVM = createCrewViewModel
        }

        initClickListener()

        initViewModelCallBack()

        createCrewViewModel.initDate()

    }

    @SuppressLint("ResourceAsColor")
    private fun initClickListener() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            btnCreateNext.setOnClickListener {
                findNavController().navigate(R.id.action_createCrewFragment2_to_createCrewFragment3)
            }

            btnCreateCrewGoalWeeks.setOnClickListener {
                initGoalWeeksDialog()
            }

            btnCreateStartDuration.setOnClickListener {
                initDatePickerDialog()
            }

            imgBtnCreateStartCalendar.setOnClickListener {
                initDatePickerDialog()
            }

            btnCrewCreateRunningStartTime.setOnClickListener {
                initStartTimeDialog()
            }

            btnCrewCreateRunningEndTime.setOnClickListener {
                initEndTimeDialog()
            }

        }
    }


    private fun initStartTimeDialog() {
        val startTimeDialog = StartTimeDialog(requireContext(), startTimeDialogListener)
        startTimeDialog.show()
    }

    private fun initEndTimeDialog() {
        val endTimeDialog = EndTimeDialog(requireContext(), endTimeDialogListener)
        endTimeDialog.show()
    }

    private fun initGoalWeeksDialog() {
        val goalWeeksDialog = GoalWeeksDialog(requireContext(), goalWeeksDialogListener)
        goalWeeksDialog.show()
    }

    private fun initDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val minDate = Calendar.getInstance()
        val maxDate = Calendar.getInstance()

        val now = System.currentTimeMillis()
        val date = Date(now)
        val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd")
        val today = dateTimeFormat.format(date)
        val todayCalendarType = dateTimeFormat.parse(today)

        calendar.time = todayCalendarType

        val dataSetListener = DatePickerDialog.OnDateSetListener { view, year, monthInt, dayOfMonthInt ->
            var month = (monthInt + 1).toString()
            if(monthInt + 1 < 10){
                month = "0" + month
            }

            var dayOfMonth = dayOfMonthInt.toString()
            if(dayOfMonthInt < 10){
                dayOfMonth = "0" + dayOfMonth
            }

            createCrewViewModel.setDateStart("${year}-${month}-${dayOfMonth}")

            createCrewViewModel.setDateEnd(year, monthInt + 1, dayOfMonthInt)
        }
        val datePickerDialog: DatePickerDialog =
            DatePickerDialog(
                requireContext(),
                dataSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

        minDate.time = todayCalendarType
        minDate.add(Calendar.DAY_OF_MONTH, 1)
        maxDate.time = todayCalendarType
        maxDate.add(Calendar.MONTH, 6)

        datePickerDialog.datePicker.minDate = minDate.time.time
        datePickerDialog.datePicker.maxDate = maxDate.timeInMillis

        datePickerDialog.show()
    }

    private val goalWeeksDialogListener: GoalWeeksDialogListener = object : GoalWeeksDialogListener {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onItemClick(amount: Int) {
            createCrewViewModel.setGoalWeeks(amount)
            createCrewViewModel.updateDateEnd()
        }
    }

    private val endTimeDialogListener: EndTimeDialogListener = object : EndTimeDialogListener {
        override fun onItemClick(hour: String, minute: String) {
            createCrewViewModel.setTimeEnd(hour, minute)
        }
    }

    private val startTimeDialogListener: StartTimeDialogListener =
        object : StartTimeDialogListener {
            override fun onItemClick(hour: String, minute: String) {
                createCrewViewModel.setTimeStart(hour, minute)
            }
        }

    private fun initViewModelCallBack() {

        createCrewViewModel.failMsgEvent.observe(viewLifecycleOwner) {
            showToast(it)
        }
    }


}