package com.ssafy.runwithme.view.running.result

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentRunningResultBinding
import com.ssafy.runwithme.model.dto.CoordinateDto
import com.ssafy.runwithme.model.dto.RunRecordDto
import com.ssafy.runwithme.model.entity.RunRecordEntity
import com.ssafy.runwithme.utils.*
import com.ssafy.runwithme.view.loading.LoadingDialog
import com.ssafy.runwithme.view.recommend.RecommendViewModel
import com.ssafy.runwithme.view.running.RunningActivity
import com.ssafy.runwithme.view.running.RunningViewModel
import com.ssafy.runwithme.view.running.result.achievement.AchievementDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

@AndroidEntryPoint
class RunningResultFragment : BaseFragment<FragmentRunningResultBinding>(R.layout.fragment_running_result) {

    private val runningViewModel by viewModels<RunningViewModel>()
    private val recommendViewModel by viewModels<RecommendViewModel>()

    private var runRecordSeq = 0

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private lateinit var loadingDialog : LoadingDialog

    private var first = true

    private var distanceText = ""
    private var timeText = ""

    override fun init() {
        distanceText = "${TrackingUtility.getFormattedDistance(RunningActivity.runRecordRunningDistance)}Km"
        timeText = TrackingUtility.getFormattedStopWatchTime(
            RunningActivity.runRecordRunningTime,
            false
        )

        initClickListener()

        initResult()

        loadingDialog = LoadingDialog(requireContext())

        if(first) {
            // 연습크루는 api 호출 x
            if (sharedPreferences.getInt(RUN_RECORD_CREW_ID, 0) == 0) {
                callLocal()
            } else {
                callApi()
            }
            first = false
        }

        initViewModelCallBack()
    }

    private fun initViewModelCallBack(){
        recommendViewModel.successMsgEvent.observe(this){
            loadingDialog.dismiss()
            showToast(it)
            requireActivity().finish()
        }
        recommendViewModel.errorMsgEvent.observe(this){
            showToast(it)
        }
        lifecycleScope.launch {
            runningViewModel.runRecordSeq.collectLatest {
                runRecordSeq = it
            }
        }

        runningViewModel.coordinateSuccess.observe(this){
            sendLatLng()
        }

        lifecycleScope.launch {
            runningViewModel.achievementsList.collectLatest {
                for(i in it){
                    AchievementDialog(requireContext(), i).show()
                }
            }
        }
    }

    private fun initResult(){
        binding.apply {
            imageView.setImageBitmap(RunningActivity.image)
            tvSpeed.text = "${RunningActivity.runRecordRunningAvgSpeed} km/h"
            tvCalorie.text = "${RunningActivity.runRecordRunningCalorie} kcal"
            tvTime.text = timeText
            tvDistance.text = distanceText
            tvRunningResultName.text = timeNameFormatter(System.currentTimeMillis())
            tvCrewName.text = sharedPreferences.getString(RUN_RECORD_CREW_NAME,"크루")
            tvTimeStartEnd.text = startEndFormatter(sharedPreferences.getLong(RUN_RECORD_START_TIME, 0L), RunningActivity.runRecordEndTime)
            tvUserName.text = sharedPreferences.getString(USER_NAME,"이름")
        }
    }

    private fun sendLatLng(){
        val list = arrayListOf<CoordinateDto>()
        for(i in RunningActivity.runPathPoints){
            for(j in i){
                list.add(CoordinateDto(j.latitude, j.longitude))
            }
        }
        Log.d(TAG, "sendLatLng: $list")
        runningViewModel.createCoordinates(runRecordSeq, list)
    }

    @Throws(IOException::class)
    private fun createFileFromBitmap(bitmap: Bitmap): File? {
        val newFile = File(requireActivity().filesDir, "record_${System.currentTimeMillis()}")
        val fileOutputStream = FileOutputStream(newFile)
        bitmap.compress(Bitmap.CompressFormat.PNG, 40, fileOutputStream)
        fileOutputStream.close()
        return newFile
    }

    private fun callLocal(){
        // 연습 러닝은 추천, 상세 경로 기능을 사용X
        binding.apply {
            btnRecommend.visibility = View.GONE
            btnRoute.visibility = View.GONE
        }
        runningViewModel.insertRun(
            RunRecordEntity(
                seq = 0,
                image = RunningActivity.image,
                startTime = timeFormatter(sharedPreferences.getLong(RUN_RECORD_START_TIME, 0L)),
                endTime = timeFormatter(RunningActivity.runRecordEndTime),
                runningTime = (RunningActivity.runRecordRunningTime / 1000).toInt(),
                runningDistance = (RunningActivity.runRecordRunningDistance).toInt(),
                avgSpeed = (RunningActivity.runRecordRunningAvgSpeed).toDouble(),
                calorie = (RunningActivity.runRecordRunningCalorie)
            )
        )
    }


    private fun callApi(){
        var imageFile: File? = null
        try {
            imageFile = createFileFromBitmap(RunningActivity.image)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), imageFile!!)
        val imgFile = MultipartBody.Part.createFormData("imgFile", imageFile!!.name, requestFile)

        val runDto = RunRecordDto(
            runRecordSeq = 0,
            runImageSeq = 0,
            runRecordStartTime = timeFormatter(sharedPreferences.getLong(RUN_RECORD_START_TIME, 0L)),
            runRecordEndTime = timeFormatter(RunningActivity.runRecordEndTime),
            runRecordRunningTime = (RunningActivity.runRecordRunningTime / 1000).toInt(),
            runRecordRunningDistance = (RunningActivity.runRecordRunningDistance).toInt(),
            runRecordRunningAvgSpeed = (RunningActivity.runRecordRunningAvgSpeed).toDouble(),
            runRecordRunningCalorie = (RunningActivity.runRecordRunningCalorie),
            runRecordRunningLat = (RunningActivity.runRecordRunningLat),
            runRecordRunningLng = (RunningActivity.runRecordRunningLng),
            runRecordRunningCompleteYN = ""
        )

        runningViewModel.createRunRecord(sharedPreferences.getInt(RUN_RECORD_CREW_ID,0) ,imgFile,runDto)
    }

    private fun initClickListener(){
        binding.apply {
            btnOk.setOnClickListener {
                requireActivity().finish()
            }
            btnRecommend.setOnClickListener {
                val action = RunningResultFragmentDirections.actionRunningResultFragmentToCreateRecommendFragment(runRecordSeq)
                findNavController().navigate(action)
            }
            btnRoute.setOnClickListener {
                val action = RunningResultFragmentDirections.actionRunningResultFragmentToRunningRouteFragment(runRecordSeq,distanceText,timeText)
                findNavController().navigate(action)
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
}