package com.ssafy.runwithme.view.running.result

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseActivity
import com.ssafy.runwithme.databinding.ActivityRunningResultBinding
import com.ssafy.runwithme.model.dto.CoordinateDto
import com.ssafy.runwithme.model.dto.RunRecordDto
import com.ssafy.runwithme.utils.*
import com.ssafy.runwithme.view.loading.LoadingDialog
import com.ssafy.runwithme.view.running.RunningActivity
import com.ssafy.runwithme.view.running.RunningViewModel
import com.ssafy.runwithme.view.running.result.achievement.AchievementDialog
import dagger.hilt.android.AndroidEntryPoint
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
class RunningResultActivity : BaseActivity<ActivityRunningResultBinding>(R.layout.activity_running_result) {

    private val runningViewModel by viewModels<RunningViewModel>()

    private var runRecordSeq = 0

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private lateinit var loadingDialog : LoadingDialog

    private var first = true
    private var achieveFirst = true

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

        loadingDialog = LoadingDialog(this)

        try {
            callApi()
        } catch (e : Exception){
            e.printStackTrace()
        }

        initViewModelCallBack()
    }

    private fun initViewModelCallBack(){

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
                if(achieveFirst && it.isNotEmpty()) {
                    for (i in it) {
                        AchievementDialog(this@RunningResultActivity, i).show()
                    }
                    achieveFirst = false
                }
            }
        }
    }

    private fun initResult(){
        binding.apply {
            tvSpeed.text = "${RunningActivity.runRecordRunningAvgSpeed} km/h"
            tvCalorie.text = "${RunningActivity.runRecordRunningCalorie} kcal"
            tvTime.text = timeText
            tvDistance.text = distanceText
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
        val newFile = File(this.filesDir, "record_${System.currentTimeMillis()}")
        val fileOutputStream = FileOutputStream(newFile)
        bitmap.compress(Bitmap.CompressFormat.PNG, 40, fileOutputStream)
        fileOutputStream.close()
        return newFile
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
                finish()
            }

        }
    }

}