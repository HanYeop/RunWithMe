package com.ssafy.runwithme.view.running.result

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseActivity
import com.ssafy.runwithme.databinding.ActivityRunningResultBinding
import com.ssafy.runwithme.model.dto.RunRecordDto
import com.ssafy.runwithme.model.entity.RunRecordEntity
import com.ssafy.runwithme.utils.*
import com.ssafy.runwithme.utils.TrackingUtility.Companion.getFormattedStopWatchTime
import com.ssafy.runwithme.view.create_recommend.CreateRecommendDialog
import com.ssafy.runwithme.view.create_recommend.CreateRecommendListener
import com.ssafy.runwithme.view.loading.LoadingDialog
import com.ssafy.runwithme.view.recommend.RecommendViewModel
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
    private val recommendViewModel by viewModels<RecommendViewModel>()

    private var runRecordSeq = 0
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private lateinit var loadingDialog : LoadingDialog
    
    override fun init() {
        initClickListener()

        initResult()

        loadingDialog = LoadingDialog(this)

        // 연습크루는 api 호출 x
        if(sharedPreferences.getInt(RUN_RECORD_CREW_ID,0) == 0){
            callLocal()
        } else{
            callApi()
        }

        initViewModelCallBack()
    }

    private fun initViewModelCallBack(){
        recommendViewModel.successMsgEvent.observe(this){
            loadingDialog.dismiss()
            showToast(it)
            finish()
        }
        recommendViewModel.errorMsgEvent.observe(this){
            showToast(it)
        }
        lifecycleScope.launch {
            runningViewModel.runRecordSeq.collectLatest {
                runRecordSeq = it
            }
        }
        lifecycleScope.launch {
            runningViewModel.achievementsList.collectLatest {
                for(i in it){
                    AchievementDialog(this@RunningResultActivity, i).show()
                }
            }
        }
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
            tvTimeStartEnd.text = startEndFormatter(sharedPreferences.getLong(RUN_RECORD_START_TIME, 0L), RunningActivity.runRecordEndTime)
            tvUserName.text = sharedPreferences.getString(USER_NAME,"이름")
        }
    }

    @Throws(IOException::class)
    private fun createFileFromBitmap(bitmap: Bitmap): File? {
        val newFile = File(this.filesDir, "record_${System.currentTimeMillis()}")
        val fileOutputStream = FileOutputStream(newFile)
        bitmap.compress(Bitmap.CompressFormat.PNG, 40, fileOutputStream)
        fileOutputStream.close()
        return newFile
    }

    private fun callLocal(){
        binding.apply {
            btnRecommend.visibility = View.GONE
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
                finish()
            }
            btnRecommend.setOnClickListener {
                CreateRecommendDialog(this@RunningResultActivity, createRecommendListener).show()
            }
        }
    }

    private val createRecommendListener = object: CreateRecommendListener {
        override fun onBtnOkClicked(environmentPoint: Int, hardPoint: Int) {
            recommendViewModel.createRecommend(environmentPoint, hardPoint, runRecordSeq)
            loadingDialog.show()
        }
    }
}