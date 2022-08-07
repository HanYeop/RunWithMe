package com.ssafy.runwithme.view.recommend

import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentRecommendDetailBinding
import com.ssafy.runwithme.model.dto.RunRecordDto
import com.ssafy.runwithme.utils.imageFormatter
import java.text.SimpleDateFormat
import java.util.*

class RecommendDetailFragment : BaseFragment<FragmentRecommendDetailBinding>(R.layout.fragment_recommend_detail) {

    private val args by navArgs<RecommendDetailFragmentArgs>()
    private var runRecordDto : RunRecordDto? = null

    private var isScrapped = false
    private var distanceText = ""
    private var timeText = ""

    override fun init() {

        runRecordDto = args.runrecordDto

        initView()

        initClickListener()
    }

    private fun initClickListener() {
        binding.apply {
            btnOk.setOnClickListener {
                findNavController().popBackStack()
            }

            imageBookmark.setOnClickListener { // 스크랩 클릭시
                if(!isScrapped){ // 스크랩 안 되어 있을 때
                    imageBookmark.setColorFilter(ContextCompat.getColor(requireContext(), R.color.main_purple))
                    isScrapped = true
                    // 스크랩 추가 로직 구현
                } else { // 이미 스크랩되어 있을 때
                    imageBookmark.colorFilter = null
                    isScrapped = false
                    // 스크랩 삭제 로직 구현
                }
            }
        }
    }

    private fun initView(){
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

        distanceText = "${Math.round(1.0 * runRecordDto!!.runRecordRunningDistance / 1000.0 * 100.0) / 100.0}km"
        timeText = "${(runRecordDto!!.runRecordRunningTime / 60)} : $second"

        binding.apply {
            imageView.imageFormatter(runRecordDto!!.runImageSeq)
            tvTime.text = timeText
            tvDistance.text = distanceText
            tvRunningResultName.text = "${startCalendar.get(Calendar.YEAR)}년 ${startCalendar.get(Calendar.MONTH) + 1}월 ${startCalendar.get(Calendar.DAY_OF_MONTH)}일 러닝"
            tvCrewName.text = runRecordDto!!.crewName
            tvTimeStartEnd.text = "${startCalendar.get(Calendar.HOUR_OF_DAY)}:${startCalendar.get(Calendar.MINUTE)}-${endCalendar.get(Calendar.HOUR_OF_DAY)}:${endCalendar.get(Calendar.MINUTE)}"
            tvUserName.text = runRecordDto!!.userName
        }
    }

}