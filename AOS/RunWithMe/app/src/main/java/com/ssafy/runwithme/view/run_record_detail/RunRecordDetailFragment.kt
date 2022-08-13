package com.ssafy.runwithme.view.run_record_detail

import android.content.SharedPreferences
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentRunRecordDetailBinding
import com.ssafy.runwithme.model.dto.RunRecordDto
import com.ssafy.runwithme.utils.TrackingUtility
import com.ssafy.runwithme.utils.USER
import com.ssafy.runwithme.utils.imageFormatter
import com.ssafy.runwithme.view.running.RunningActivity
import com.ssafy.runwithme.view.running.result.RunningResultFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Math.round
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class RunRecordDetailFragment : BaseFragment<FragmentRunRecordDetailBinding>(R.layout.fragment_run_record_detail) {

    @Inject
    lateinit var sharedPref: SharedPreferences
    private val args by navArgs<RunRecordDetailFragmentArgs>()
    private var runRecordDto : RunRecordDto? = null
    private var userSeq : Int = 0

    private var distanceText = ""
    private var timeText = ""

    override fun init() {

        runRecordDto = args.runrecorddto

        userSeq = sharedPref.getString(USER, "0")!!.toInt()

        initViewVisible()

        initResult()

        initClickListener()
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
                val action = RunRecordDetailFragmentDirections.actionRunRecordDetailFragmentToCreateRecommendFragment2(runRecordDto!!.runRecordSeq)
                findNavController().navigate(action)
            }
            imageView.setOnClickListener{
                val action = RunRecordDetailFragmentDirections.actionRunRecordDetailFragmentToRunningRouteFragment2(
                    runRecordDto!!.runRecordSeq,distanceText,timeText)
                findNavController().navigate(action)
            }
            btnRoute.setOnClickListener {
                val action = RunRecordDetailFragmentDirections.actionRunRecordDetailFragmentToRunningRouteFragment2(
                    runRecordDto!!.runRecordSeq,distanceText,timeText)
                findNavController().navigate(action)
            }
        }
    }

    private fun initResult(){
        val secondInt = runRecordDto!!.runRecordRunningTime % 60
        var second = secondInt.toString()
        if(secondInt < 10){
            second = "0$second"
        }

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val start = dateFormat.parse(runRecordDto!!.runRecordStartTime)
        val end = dateFormat.parse(runRecordDto!!.runRecordEndTime)

        val startCalendar = Calendar.getInstance()
        val endCalendar = Calendar.getInstance()
        startCalendar.time = start
        endCalendar.time = end

        distanceText = "${round(1.0 * runRecordDto!!.runRecordRunningDistance / 1000.0 * 100.0) / 100.0}km"
        timeText = "${(runRecordDto!!.runRecordRunningTime / 60)} : $second"

        binding.apply {
            imageView.imageFormatter(runRecordDto!!.runImageSeq)
            tvSpeed.text = "${round(runRecordDto!!.runRecordRunningAvgSpeed * 10.0) / 10.0} km/h"
            tvCalorie.text = "${runRecordDto!!.runRecordRunningCalorie} kcal"
            tvTime.text = timeText
            tvDistance.text = distanceText
            tvRunningResultName.text = "${startCalendar.get(Calendar.YEAR)}년 ${startCalendar.get(Calendar.MONTH) + 1}월 ${startCalendar.get(Calendar.DAY_OF_MONTH)}일 러닝"
            tvCrewName.text = runRecordDto!!.crewName
            tvTimeStartEnd.text = "${startCalendar.get(Calendar.HOUR_OF_DAY)}:${startCalendar.get(Calendar.MINUTE)}-${endCalendar.get(Calendar.HOUR_OF_DAY)}:${endCalendar.get(Calendar.MINUTE)}"
            tvUserName.text = runRecordDto!!.userName

            if (runRecordDto!!.runRecordRunningCompleteYN == "Y") {
                Glide.with(requireActivity()).load(R.drawable.success_stamp).into(binding.imgComplete)
            }else{
                Glide.with(requireActivity()).load(R.drawable.fail_stamp).into(binding.imgComplete)
            }
        }
    }
}