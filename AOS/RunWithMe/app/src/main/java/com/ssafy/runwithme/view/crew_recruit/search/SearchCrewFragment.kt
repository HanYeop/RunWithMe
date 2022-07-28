package com.ssafy.runwithme.view.crew_recruit.search

import android.app.DatePickerDialog
import android.os.Build
import android.view.View
import android.widget.RadioGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentSearchCrewBinding
import com.ssafy.runwithme.view.crew_recruit.*
import java.text.SimpleDateFormat
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class SearchCrewFragment : BaseFragment<FragmentSearchCrewBinding>(R.layout.fragment_search_crew) {

    private val searchCrewViewModel by viewModels<SearchCrewViewModel>()

    override fun init() {
        binding.apply {
            searchCrewVM = searchCrewViewModel
        }

        initClickListener()

        initViewModelCallBack()

        initCheckboxClickChangeLisnster()

        initRadioGroupCheck()

        searchCrewViewModel.initDate()
    }

    private fun initClickListener() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            // 비즈니스 로직 구현하기
            btnSearch.setOnClickListener {

                var crewName = ""
                if(searchCrewViewModel.crewName.value != ""){
                    crewName = searchCrewViewModel.crewName.value
                }

                var startDate = ""
                var endDate = ""
                if(checkboxCrewDuration.isChecked){
                    startDate = searchCrewViewModel.dateStart.value
                    endDate = searchCrewViewModel.dateEnd.value
                }


                var startTime = ""
                var endTime = ""
                if(checkboxCrewRunningStartTime.isChecked){
                    startTime = searchCrewViewModel.timeStart.value
                    endTime = searchCrewViewModel.timeEnd.value
                }

                var minCost = 0
                var maxCost = 100000
                if(checkboxCrewCost.isChecked){
                    minCost = searchCrewViewModel.minCost.value.toInt()
                    maxCost = searchCrewViewModel.maxCost.value.toInt()
                }

                var goalType = ""
                if(checkboxCrewPurpose.isChecked){
                    val check = radioGroupPurpose.checkedRadioButtonId
                    if(check == R.id.radio_btn_time){
                        goalType = "time"
                    }else{
                        goalType = "distance"
                    }
                }

                var minPurPoseAmount = 0
                var maxPurPoseAmount = 600
                if(checkboxSearchGoalAmount.isChecked){
                    if(checkboxCrewPurpose.isChecked){
                        val check = radioGroupPurpose.checkedRadioButtonId
                        if(check == R.id.radio_btn_time){
                            minPurPoseAmount = searchCrewViewModel.minTime.value.toInt() * 60
                            maxPurPoseAmount = searchCrewViewModel.maxTime.value.toInt() * 60
                        }else{
                            minPurPoseAmount = searchCrewViewModel.minDistance.value.toInt() * 1000
                            maxPurPoseAmount = searchCrewViewModel.maxDistance.value.toInt() * 1000
                        }

                    }
                }

                var minGoalDays = 0
                var maxGoalDays = 7
                if(checkboxSearchGoalDyas.isChecked){
                    minGoalDays = searchCrewViewModel.minGoalDays.value.toInt()
                    maxGoalDays = searchCrewViewModel.maxGoalDays.value.toInt()
                }

                val action = SearchCrewFragmentDirections.actionSearchCrewFragmentToSearchCrewResultFragment(crewName, startDate, endDate, startTime, endTime, minCost, maxCost, minPurPoseAmount, maxPurPoseAmount, minGoalDays, maxGoalDays, goalType)
                findNavController().navigate(action)
            }

            btnSearchStartDuration.setOnClickListener {
                initStartDateDailog()
            }

            btnSearchEndDuration.setOnClickListener {
                initEndDateDialog()
            }

            btnCrewRunningStartTime.setOnClickListener {
                initStartTimeDateDialog()
            }

            btnCrewRunningEndTime.setOnClickListener {
                initEndTimeDateDialog()
            }

            btnSearchMinCost.setOnClickListener {
                initMinCostDialog()
            }

            btnSearchMaxCost.setOnClickListener {
                initMaxCostDialog()
            }

            btnSearchGoalAmountStart.setOnClickListener {
                val check = radioGroupPurpose.checkedRadioButtonId
                if(check == R.id.radio_btn_time){
                    initPurposeMinTimeDialog()
                }else{
                    initPurposeMinDistanceDialog()
                }
            }

            btnSearchGoalAmountEnd.setOnClickListener {
                val check = radioGroupPurpose.checkedRadioButtonId
                if(check == R.id.radio_btn_time){
                    initPurposeMaxTimeDialog()
                }else{
                    initPurposeMaxDistanceDialog()
                }
            }

            btnSearchMinGoalDays.setOnClickListener {
                initMinGoalDaysDialog()
            }

            btnSearchMaxGoalDays.setOnClickListener {
                initMaxGoalDaysDialog()
            }
        }
    }

    private fun initStartDateDailog() {
        val calendar = Calendar.getInstance()
        val minDate = Calendar.getInstance()

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

            searchCrewViewModel.setDateStart("${year}-${month}-${dayOfMonth}")

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

        datePickerDialog.datePicker.minDate = minDate.time.time

        datePickerDialog.show()
    }

    private fun initEndDateDialog() {
        val calendar = Calendar.getInstance()
        val minDate = Calendar.getInstance()

        val now = System.currentTimeMillis()
        val date = Date(now)
        val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd")
        val today = dateTimeFormat.format(date)
        val minDay = searchCrewViewModel.dateStart.value
        val todayCalendarType = dateTimeFormat.parse(today)
        val minDateCalendarType = dateTimeFormat.parse(minDay)

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

            searchCrewViewModel.setDateEnd("${year}-${month}-${dayOfMonth}")

        }
        val datePickerDialog: DatePickerDialog =
            DatePickerDialog(
                requireContext(),
                dataSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

        minDate.time = minDateCalendarType
        minDate.add(Calendar.DAY_OF_MONTH, 1)

        datePickerDialog.datePicker.minDate = minDate.time.time

        datePickerDialog.show()
    }

    private fun initStartTimeDateDialog() {
        val startTimeDialog = StartTimeDialog(requireContext(), startTimeDialogListener)
        startTimeDialog.show()
    }

    private fun initEndTimeDateDialog() {
        val endTimeDialog = EndTimeDialog(requireContext(), endTimeDialogListener)
        endTimeDialog.show()
    }

    private fun initMinGoalDaysDialog() {
        val goalDaysDialog = GoalDaysDialog(requireContext(), goalMinDaysDialogListener, 1)
        goalDaysDialog.show()
    }

    private fun initMaxGoalDaysDialog() {
        val goalDaysDialog = GoalDaysDialog(requireContext(), goalMaxDaysDialogListener, searchCrewViewModel.minGoalDays.value.toInt())
        goalDaysDialog.show()
    }

    private fun initMinCostDialog() {
        val costDialog = CostDialog(requireContext(), minCostDialogListener)
        costDialog.show()
    }

    private fun initMaxCostDialog() {
        val costDialog = CostDialog(requireContext(), maxCostDialogListener)
        costDialog.show()
    }

    private fun initPurposeMinDistanceDialog() {
        val purposeDistanceDialog = PurposeDistanceDialog(requireContext(), purposeMinDistanceDialogListener, 1)
        purposeDistanceDialog.show()
    }

    private fun initPurposeMaxDistanceDialog() {
        val purposeDistanceDialog = PurposeDistanceDialog(requireContext(), purposeMaxDistanceDialogListener, searchCrewViewModel.minDistance.value.toInt())
        purposeDistanceDialog.show()
    }

    private fun initPurposeMinTimeDialog() {
        val purposeTimeDialog = PurposeTimeDialog(requireContext(), purposeMinTimeDialogListener)
        purposeTimeDialog.show()
    }

    private fun initPurposeMaxTimeDialog() {
        val purposeTimeDialog = PurposeTimeDialog(requireContext(), purposeMaxTimeDialogListener)
        purposeTimeDialog.show()
    }

    private val startTimeDialogListener : StartTimeDialogListener = object : StartTimeDialogListener {
        override fun onItemClick(hour: String, minute: String) {
            searchCrewViewModel.setTimeStart(hour, minute)
        }
    }

    private val endTimeDialogListener : EndTimeDialogListener = object : EndTimeDialogListener {
        override fun onItemClick(hour: String, minute: String) {
            searchCrewViewModel.setTimeEnd(hour, minute)
        }
    }

    private val goalMinDaysDialogListener : GoalDaysDialogListener = object : GoalDaysDialogListener {
        override fun onItemClick(days: Int) {
            searchCrewViewModel.setMinGoalDays(days)
        }
    }

    private val goalMaxDaysDialogListener : GoalDaysDialogListener = object : GoalDaysDialogListener {
        override fun onItemClick(days: Int) {
            searchCrewViewModel.setMaxGoalDays(days)
        }
    }

    private val minCostDialogListener : CostDialogListener = object : CostDialogListener {
        override fun onItemClick(cost: String) {
            searchCrewViewModel.setMinCost(cost)
        }
    }

    private val maxCostDialogListener : CostDialogListener = object : CostDialogListener {
        override fun onItemClick(cost: String) {
            searchCrewViewModel.setMaxCost(cost)
        }
    }

    private val purposeMinTimeDialogListener : PurposeTimeDialogListener = object :
        PurposeTimeDialogListener {
        override fun onItemClick(time: String) {
            searchCrewViewModel.setMinTime(time)
        }
    }

    private val purposeMaxTimeDialogListener : PurposeTimeDialogListener = object :
        PurposeTimeDialogListener {
        override fun onItemClick(time: String) {
            searchCrewViewModel.setMaxTime(time)
        }
    }

    private val purposeMinDistanceDialogListener : PurposeDistanceDialogListener = object :
        PurposeDistanceDialogListener {
        override fun onItemClick(distance: Int) {
            searchCrewViewModel.setMinDistance(distance)
        }
    }

    private val purposeMaxDistanceDialogListener : PurposeDistanceDialogListener = object :
        PurposeDistanceDialogListener {
        override fun onItemClick(distance: Int) {
            searchCrewViewModel.setMaxDistance(distance)
        }
    }

    private fun initViewModelCallBack() {
        searchCrewViewModel.failMsgEvent.observe(viewLifecycleOwner) {
            showToast(it)
        }
    }

    private fun initCheckboxClickChangeLisnster() {
        binding.apply {
            checkboxCrewDuration.setOnCheckedChangeListener { button, isChecked ->
                if(isChecked){
                    btnSearchStartDuration.apply{
                        alpha = 1F
                        isEnabled = true
                    }
                    btnSearchEndDuration.apply{
                        alpha = 1F
                        isEnabled = true
                    }
                }else{
                    btnSearchStartDuration.apply{
                        alpha = 0.5F
                        isEnabled = false
                    }
                    btnSearchEndDuration.apply{
                        alpha = 0.5F
                        isEnabled = false
                    }
                }
            }

            checkboxCrewRunningStartTime.setOnCheckedChangeListener { button, isChecked ->
                if(isChecked){
                    btnCrewRunningStartTime.apply{
                        alpha = 1F
                        isEnabled = true
                    }
                    btnCrewRunningEndTime.apply{
                        alpha = 1F
                        isEnabled = true
                    }
                }else{
                    btnCrewRunningStartTime.apply{
                        alpha = 0.5F
                        isEnabled = false
                    }
                    btnCrewRunningEndTime.apply{
                        alpha = 0.5F
                        isEnabled = false
                    }
                }
            }

            checkboxCrewCost.setOnCheckedChangeListener { button, isChecked ->
                if(isChecked){
                    btnSearchMinCost.apply{
                        alpha = 1F
                        isEnabled = true
                    }
                    btnSearchMaxCost.apply{
                        alpha = 1F
                        isEnabled = true
                    }
                }else{
                    btnSearchMinCost.apply{
                        alpha = 0.5F
                        isEnabled = false
                    }
                    btnSearchMaxCost.apply{
                        alpha = 0.5F
                        isEnabled = false
                    }
                }
            }

            checkboxCrewPurpose.setOnCheckedChangeListener { button, isChecked ->
                if(isChecked){
                    radioBtnTime.apply{
                        alpha = 1F
                        isEnabled = true
                    }
                    radioBtnDistance.apply{
                        alpha = 1F
                        isEnabled = true
                    }
                    checkboxSearchGoalAmount.apply {
                        alpha = 1F
                        isEnabled = true
                    }
                }else{
                    radioBtnTime.apply{
                        alpha = 0.5F
                        isEnabled = false
                    }
                    radioBtnDistance.apply{
                        alpha = 0.5F
                        isEnabled = false
                    }
                    checkboxSearchGoalAmount.apply {
                        alpha = 0.5F
                        isEnabled = false
                        setChecked(false)
                    }
                    btnSearchGoalAmountStart.apply {
                        alpha = 0.5F
                        isEnabled = false
                    }
                    btnSearchGoalAmountEnd.apply {
                        alpha = 0.5F
                        isEnabled = false
                    }
                }
            }

            checkboxSearchGoalAmount.setOnCheckedChangeListener { button, isChecked ->
                if(isChecked){
                    btnSearchGoalAmountStart.apply{
                        alpha = 1F
                        isEnabled = true
                    }
                    btnSearchGoalAmountEnd.apply{
                        alpha = 1F
                        isEnabled = true
                    }

                    if(radioBtnDistance.isChecked){
                        tvGoalType.apply {
                            visibility = View.VISIBLE
                            text = "km"
                        }
                    }

                    if(radioBtnTime.isChecked){
                        tvGoalType.apply {
                            visibility = View.VISIBLE
                            text = "분"
                        }
                    }

                }else{
                    btnSearchGoalAmountStart.apply{
                        alpha = 1F
                        isEnabled = true
                    }
                    btnSearchGoalAmountEnd.apply{
                        alpha = 1F
                        isEnabled = true
                    }

                    if(radioBtnDistance.isChecked){
                        tvGoalType.apply {
                            visibility = View.INVISIBLE
                        }
                    }

                    if(radioBtnTime.isChecked){
                        tvGoalType.apply {
                            visibility = View.INVISIBLE
                        }
                    }
                }
            }

            checkboxSearchGoalDyas.setOnCheckedChangeListener { button, isChecked ->
                if(isChecked){
                    btnSearchMinGoalDays.apply{
                        alpha = 1F
                        isEnabled = true
                    }
                    btnSearchMaxGoalDays.apply{
                        alpha = 1F
                        isEnabled = true
                    }
                }else{
                    btnSearchMinGoalDays.apply{
                        alpha = 0.5F
                        isEnabled = false
                    }
                    btnSearchMaxGoalDays.apply{
                        alpha = 0.5F
                        isEnabled = false
                    }
                }
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
                            searchCrewViewModel.setGoalTypeDistance(false)
                        }
                        R.id.radio_btn_distance -> {
                            tvGoalType.text = "km"
                            searchCrewViewModel.setGoalTypeDistance(true)
                        }
                    }
                }
            })
        }
    }

}