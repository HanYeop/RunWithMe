package com.ssafy.runwithme.view.run_record_detail

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentRunRecordDetailBinding
import com.ssafy.runwithme.model.dto.RunRecordDto
import com.ssafy.runwithme.utils.*
import com.ssafy.runwithme.view.running.RunningActivity
import java.lang.Math.round
import java.text.SimpleDateFormat

class RunRecordDetailFragment : BaseFragment<FragmentRunRecordDetailBinding>(R.layout.fragment_run_record_detail) {

    private val args by navArgs<RunRecordDetailFragmentArgs>()
    private var runRecordDto : RunRecordDto? = null

    override fun init() {

        runRecordDto = args.runrecorddto

        initClickListener()

        initResult()
    }

    private fun initClickListener() {
        binding.apply {
            btnOk.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }


    private fun initResult(){
        val secondInt = runRecordDto!!.runRecordRunningTime % 60
        var second = secondInt.toString()
        if(secondInt < 10){
            second = "0" + second
        }
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val start = dateFormat.format(runRecordDto!!.runRecordStartTime)
        val end = dateFormat.format(runRecordDto!!.runRecordEndTime)

        binding.apply {
            imgResult.setImageBitmap(RunningActivity.image)
            tvSpeed.text = "${round(runRecordDto!!.runRecordRunningAvgSpeed * 10.0) / 10} km/h"
            tvCalorie.text = "${runRecordDto!!.runRecordRunningCalorie} kcal"
            tvTime.text = "${(runRecordDto!!.runRecordRunningTime / 60)} : $second"
            tvDistance.text = "${round(1.0 * runRecordDto!!.runRecordRunningDistance / 1000.0 * 100) / 100} km"
            tvRunningResultName.text = "${start[0]}년 ${start[1]}월 ${start[2]}일 러닝"
            tvCrewName.text = runRecordDto!!.crewName
            tvTimeStartEnd.text = "${start[3]}:${start[4]}-${end[3]}:${end[4]}"
            tvUserName.text = runRecordDto!!.userName
        }
    }
}