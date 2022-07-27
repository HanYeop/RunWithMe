package com.ssafy.runwithme.view.crew_recruit.create

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.RadioGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentCreateCrewBinding
import com.ssafy.runwithme.model.dto.CrewDto
import com.ssafy.runwithme.view.crew_recruit.CrewRecruitViewModel
import com.ssafy.runwithme.view.running.RunningActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URI
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class CreateCrewFragment : BaseFragment<FragmentCreateCrewBinding>(R.layout.fragment_create_crew) {

    private val crewRecruitViewModel by viewModels<CrewRecruitViewModel>()
    private var imgFile : MultipartBody.Part? = null

    override fun init() {

        binding.apply {
            crewRecruitVm = crewRecruitViewModel
        }

        initClickListener()

        initViewModelCallBack()

        initRadioGroupCheck()

        crewRecruitViewModel.initDate()

        //initImageFile()
    }
//
//    private fun initImageFile(){
//        val resources = requireContext().resources
//        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.crew_image)
//        createMultiPart(bitmap)
//    }

    @SuppressLint("ResourceAsColor")
    private fun initClickListener() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            btnCreate.setOnClickListener {
                crewRecruitViewModel.createCrew(imgFile)
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

            btnCreateCrewPasswd.setOnClickListener {
                initPasswdDialog()
            }

            btnCreateCrewMaxMember.setOnClickListener {
                initMaxMemberDialog()
            }

            btnCreateCrewCost.setOnClickListener {
                initCostDialog()
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

            imgCreateCrew.setOnClickListener {
                pickPhotoGallery()
            }
        }
    }

    private fun initRadioGroupCheck() {
        binding.apply {
            radioGroupPasswd.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener{
                @SuppressLint("ResourceAsColor")
                override fun onCheckedChanged(radioGroup: RadioGroup?, checkId: Int) {
                    when(checkId){
                        R.id.radio_btn_passwd_on ->{
                            binding.apply {
                                btnCreateCrewPasswd.visibility = View.GONE
                                tvPasswd.visibility = View.GONE
                                crewRecruitViewModel.setPasswd("")
                            }
                        }

                        R.id.radio_btn_passwd_off -> {
                            binding.apply {
                                btnCreateCrewPasswd.visibility = View.VISIBLE
                                tvPasswd.visibility = View.VISIBLE
                            }
                        }
                    }
                }
            })

            radioGroupPurpose.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener{
                override fun onCheckedChanged(radioGroup: RadioGroup?, checkId: Int) {
                    when(checkId){
                        R.id.radio_btn_time -> {
                            tvGoalType.text = "분"
                            crewRecruitViewModel.setGoalTypeDistance(false)
                        }
                        R.id.radio_btn_distance -> {
                            tvGoalType.text = "km"
                            crewRecruitViewModel.setGoalTypeDistance(true)
                        }
                    }
                }
            })
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

    private fun initGoalDaysDialog() {
        val goalDaysDialog = GoalDaysDialog(requireContext(), goalDaysDialogListener)
        goalDaysDialog.show()
    }

    private fun initGoalWeeksDialog() {
        val goalWeeksDialog = GoalWeeksDialog(requireContext(), goalWeeksDialogListener)
        goalWeeksDialog.show()
    }

    private fun initCostDialog() {
        val costDialog = CostDialog(requireContext(), costDialogListener)
        costDialog.show()
    }

    private fun initMaxMemberDialog() {
        val maxMemberDialog = MaxMemberDialog(requireContext(), maxMemberDialogListener)
        maxMemberDialog.show()
    }

    private fun initPurposeDistanceDialog() {
        val purposeDistanceDialog = PurposeDistanceDialog(requireContext(), purposeDistanceDialogListener)
        purposeDistanceDialog.show()
    }

    private fun initPurposeTimeDialog() {
        val purposeTimeDialog = PurposeTimeDialog(requireContext(), purposeTimeDialogListener)
        purposeTimeDialog.show()
    }

    private fun initPasswdDialog() {
        val passwdDialog = PasswdDialog(requireContext(), passwdDialogListener)
        passwdDialog.show()
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

            crewRecruitViewModel.setDateStart("${year}-${month}-${dayOfMonth}")

            crewRecruitViewModel.setDateEnd(year, monthInt + 1, dayOfMonthInt)
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
            crewRecruitViewModel.setGoalWeeks(amount)
            crewRecruitViewModel.updateDateEnd()
        }
    }

    private val maxMemberDialogListener: MaxMemberDialogListener = object : MaxMemberDialogListener {
        override fun onItemClick(max: Int) {
            crewRecruitViewModel.setMaxMember(max)
        }
    }

    private val endTimeDialogListener: EndTimeDialogListener = object : EndTimeDialogListener {
        override fun onItemClick(hour: String, minute: String) {
            crewRecruitViewModel.setTimeEnd(hour, minute)
        }
    }

    private val startTimeDialogListener: StartTimeDialogListener =
        object : StartTimeDialogListener {
            override fun onItemClick(hour: String, minute: String) {
                crewRecruitViewModel.setTimeStart(hour, minute)
            }
        }

    private val passwdDialogListener: PasswdDialogListener = object : PasswdDialogListener {
        override fun onItemClick(passwd: String) {
            crewRecruitViewModel.setPasswd(passwd)
        }
    }

    private val goalDaysDialogListener : GoalDaysDialogListener = object : GoalDaysDialogListener {
        override fun onItemClick(days: Int) {
            crewRecruitViewModel.setGoalDays(days)
        }
    }

    private val costDialogListener : CostDialogListener = object : CostDialogListener {
        override fun onItemClick(cost: String) {
            crewRecruitViewModel.setCost(cost)
        }
    }

    private val purposeTimeDialogListener : PurposeTimeDialogListener = object : PurposeTimeDialogListener {
        override fun onItemClick(time: String) {
            crewRecruitViewModel.setTime(time)
        }
    }

    private val purposeDistanceDialogListener : PurposeDistanceDialogListener = object : PurposeDistanceDialogListener {
        override fun onItemClick(distance: Int) {
            crewRecruitViewModel.setDistance(distance)
        }
    }

    private fun initViewModelCallBack() {
        crewRecruitViewModel.errorMsgEvent.observe(viewLifecycleOwner) {
            showToast(it)
        }

        crewRecruitViewModel.successMsgEvent.observe(viewLifecycleOwner) {
            showToast(it)
            findNavController().popBackStack()
        }

        crewRecruitViewModel.failMsgEvent.observe(viewLifecycleOwner) {
            showToast(it)
        }
    }

    private fun pickPhotoGallery() {
        val photoIntent = Intent(Intent.ACTION_PICK)
        photoIntent.type = "image/*"
        pickPhotoResult.launch(photoIntent)
    }

    private val pickPhotoResult : ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == Activity.RESULT_OK){
            binding.imgCreateCrew.setImageURI(it.data?.data)
            var bitmap : Bitmap? = null
            val uri = it.data?.data
            try{
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                    bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireActivity().contentResolver, uri!!))
                }else{
                    bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)
                }
            }catch (e : Exception){
                e.printStackTrace()
            }finally {
                createMultiPart(bitmap!!)
            }
        }
    }

    @Throws(IOException::class)
    private fun createFileFromBitmap(bitmap: Bitmap): File {
        val newFile = File(requireActivity().filesDir, "test")
        val fileOutputStream = FileOutputStream(newFile)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
        fileOutputStream.close()
        return newFile
    }



    private fun createMultiPart(bitmap: Bitmap) {
        var imageFile: File? = null
        try {
            imageFile = createFileFromBitmap(bitmap)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), imageFile!!)
        imgFile = MultipartBody.Part.createFormData("imgFile", imageFile!!.name, requestFile)

    }

}