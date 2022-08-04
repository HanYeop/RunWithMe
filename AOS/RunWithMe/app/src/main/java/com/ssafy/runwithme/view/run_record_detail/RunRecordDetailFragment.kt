package com.ssafy.runwithme.view.run_record_detail

import android.content.SharedPreferences
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentRunRecordDetailBinding
import com.ssafy.runwithme.model.dto.RunRecordDto
import com.ssafy.runwithme.utils.*
import com.ssafy.runwithme.view.create_recommend.CreateRecommendDialog
import com.ssafy.runwithme.view.create_recommend.CreateRecommendListener
import com.ssafy.runwithme.view.running.RunningActivity
import com.ssafy.runwithme.view.running.result.achievement.AchievementDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.lang.Math.round
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.time.Duration.Companion.hours

@AndroidEntryPoint
class RunRecordDetailFragment : BaseFragment<FragmentRunRecordDetailBinding>(R.layout.fragment_run_record_detail) {

    @Inject
    lateinit var sharedPref: SharedPreferences
    private val args by navArgs<RunRecordDetailFragmentArgs>()
    private var runRecordDto : RunRecordDto? = null
    private var userSeq : Int = 0
    private val runRecordDetailViewModel by viewModels<RunRecordDetailViewModel>()

    override fun init() {

        runRecordDto = args.runrecorddto

        userSeq = sharedPref.getString(USER, "0")!!.toInt()

        initViewVisible()

        initClickListener()

        initViewModelCallBack()

        initResult()
    }

    private fun initViewVisible(){
        if(userSeq != runRecordDto!!.userSeq){
            binding.apply {
                btnRecommend.visibility = View.GONE
            }
        }
    }

    private fun initClickListener() {
        binding.apply {
            btnOk.setOnClickListener {
                findNavController().popBackStack()
            }
            btnRecommend.setOnClickListener {
                CreateRecommendDialog(requireContext(), createRecommendListener).show()
            }
        }
    }

    private fun initViewModelCallBack(){
        runRecordDetailViewModel.successMsgEvent.observe(this){
            showToast(it)
            findNavController().popBackStack()
        }
        runRecordDetailViewModel.errorMsgEvent.observe(this){
            showToast(it)
        }
    }

    private val createRecommendListener = object: CreateRecommendListener {
        override fun onBtnOkClicked(environmentPoint: Int, hardPoint: Int) {
            runRecordDetailViewModel.createRecommend(environmentPoint, hardPoint, runRecordDto!!.runRecordSeq)
        }
    }


    private fun initResult(){
        val secondInt = runRecordDto!!.runRecordRunningTime % 60
        var second = secondInt.toString()
        if(secondInt < 10){
            second = "0" + second
        }

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val start = dateFormat.parse(runRecordDto!!.runRecordStartTime)
        val end = dateFormat.parse(runRecordDto!!.runRecordEndTime)

        val startCalendar = Calendar.getInstance()
        val endCalendar = Calendar.getInstance()
        startCalendar.time = start
        endCalendar.time = end

        binding.apply {
            imgResult.imageFormatter(runRecordDto!!.runImageSeq)
            tvSpeed.text = "${round(runRecordDto!!.runRecordRunningAvgSpeed * 10.0) / 10.0} km/h"
            tvCalorie.text = "${runRecordDto!!.runRecordRunningCalorie} kcal"
            tvTime.text = "${(runRecordDto!!.runRecordRunningTime / 60)} : $second"
            tvDistance.text = "${round(1.0 * runRecordDto!!.runRecordRunningDistance / 1000.0 * 100.0) / 100.0} km"
//            tvRunningResultName.text = "${start[0]}년 ${start[1]}월 ${start[2]}일 러닝"
            tvRunningResultName.text = "${startCalendar.get(Calendar.YEAR)}년 ${startCalendar.get(Calendar.MONTH) + 1}월 ${startCalendar.get(Calendar.DAY_OF_MONTH)}일 러닝"
            tvCrewName.text = runRecordDto!!.crewName
            tvTimeStartEnd.text = "${startCalendar.get(Calendar.HOUR_OF_DAY)}:${startCalendar.get(Calendar.MINUTE)}-${endCalendar.get(Calendar.HOUR_OF_DAY)}:${endCalendar.get(Calendar.MINUTE)}"
//            tvTimeStartEnd.text = "${start[3]}:${start[4]}-${end[3]}:${end[4]}"
            tvUserName.text = runRecordDto!!.userName
        }
    }
}