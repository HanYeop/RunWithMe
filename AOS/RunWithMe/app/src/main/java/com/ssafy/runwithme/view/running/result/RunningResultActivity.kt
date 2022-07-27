package com.ssafy.runwithme.view.running.result

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.util.Log
import androidx.activity.viewModels
import com.google.gson.annotations.SerializedName
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseActivity
import com.ssafy.runwithme.databinding.ActivityRunningResultBinding
import com.ssafy.runwithme.model.dto.RunRecordDto
import com.ssafy.runwithme.utils.*
import com.ssafy.runwithme.utils.TrackingUtility.Companion.getFormattedStopWatchTime
import com.ssafy.runwithme.view.create_recommend.CreateRecommendDialog
import com.ssafy.runwithme.view.running.RunningActivity
import com.ssafy.runwithme.view.running.RunningViewModel
import com.ssafy.runwithme.view.running.result.achievement.AchievementDialog
import dagger.hilt.android.AndroidEntryPoint
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
    @Inject
    lateinit var sharedPreferences: SharedPreferences
    
    override fun init() {
        initClickListener()

        initResult()

        callApi()

        // TEST
        AchievementDialog(this).show()
    }

    private fun initResult(){
        binding.apply {
            imgResult.setImageBitmap(RunningActivity.image)
            tvSpeed.text = "${RunningActivity.runRecordRunningAvgSpeed} km/h"
            tvCalorie.text = "${RunningActivity.runRecordRunningCalorie} kcal"
            tvTime.text = "${getFormattedStopWatchTime(RunningActivity.runRecordRunningTime,false)}"
            tvDistance.text = "${TrackingUtility.getFormattedDistance(RunningActivity.runRecordRunningDistance)}Km"
            tvRunningResultName.text = timeNameFormatter(System.currentTimeMillis())
            tvCrewName.text = sharedPreferences.getString(RUN_RECORD_CREW_NAME,"크루")
            Log.d(TAG, "initResult: ${timeFormatter(sharedPreferences.getLong(RUN_RECORD_START_TIME,0))}")
            Log.d(TAG, "initResult: ${sharedPreferences.getInt(RUN_RECORD_CREW_ID,0)}")
            Log.d(TAG, "initResult: ${RunningActivity.runRecordRunningLat} ${RunningActivity.runRecordRunningLng}")
//            RunningActivity.runRecordEndTime = timeFormatter(System.currentTimeMillis())
//            RunningActivity.runRecordRunningAvgSpeed = (round((sumDistance / 1000f) / (currentTimeInMillis / 1000f / 60 / 60) * 10) / 10f).toDouble()
//            RunningActivity.runRecordRunningCalorie = caloriesBurned
//            RunningActivity.runRecordRunningDistance = (sumDistance * 1000).toInt()
//            RunningActivity.runRecordRunningLat = pathPoints.first()[0].latitude
//            RunningActivity.runRecordRunningLng = pathPoints.first()[0].longitude
//            RunningActivity.runRecordRunningTime = currentTimeInMillis.toInt()
        }
    }

    @Throws(IOException::class)
    private fun createFileFromBitmap(bitmap: Bitmap): File? {
        val newFile = File(this.filesDir, "test")
        val fileOutputStream = FileOutputStream(newFile)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
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
            runRecordStartTime = timeFormatter(System.currentTimeMillis()),
            runRecordEndTime = timeFormatter(System.currentTimeMillis()),
            runRecordRunningTime = 1,
            runRecordRunningDistance = 2,
            runRecordRunningAvgSpeed = 1.1,
            runRecordRunningCalorie = 1,
            runRecordRunningLat = 37.111,
            runRecordRunningLng = 127.1111,
            runRecordRunningCompleteYN = "Y"
        )

        runningViewModel.createRunRecord(imgFile,runDto)
    }

    private fun initClickListener(){
        binding.apply {
            btnOk.setOnClickListener {
                finish()
            }
            btnRecommend.setOnClickListener {
                CreateRecommendDialog(this@RunningResultActivity).show()
            }
        }
    }
}