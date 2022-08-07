package com.ssafy.runwithme.view.recommend

import android.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentRecommendDetailBinding
import com.ssafy.runwithme.model.dto.RunRecordDto
import com.ssafy.runwithme.model.dto.TrackBoardDto
import com.ssafy.runwithme.utils.imageFormatter
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class RecommendDetailFragment : BaseFragment<FragmentRecommendDetailBinding>(R.layout.fragment_recommend_detail) {

    private val args by navArgs<RecommendDetailFragmentArgs>()
    private var runRecordDto : RunRecordDto? = null
    private var trackBoardDto : TrackBoardDto? = null
    private var title = ""

    private val recommendDetailViewModel by viewModels<RecommendDetailViewModel>()

    private var isScrapped = false
    private var distanceText = ""
    private var timeText = ""
    private var currentScrapSeq = 0

    override fun init() {
        runRecordDto = args.runrecordDto
        trackBoardDto = args.trackBoardDto

        recommendDetailViewModel.getMyScrap(trackBoardDto!!.trackBoardSeq) // 이미 나에게 스크랩된 경로인지 확인

        initView()

        initClickListener()

        initViewModelCallBack()
    }

    private fun initClickListener() {
        binding.apply {
            btnOk.setOnClickListener {
                findNavController().popBackStack()
            }

            imageBookmark.setOnClickListener { // 스크랩 클릭시
                var dialog = AlertDialog.Builder(requireContext()).setNegativeButton("취소") { _, _ -> }

                if(!isScrapped){ // 스크랩 안 되어 있을 때
                    dialog.setTitle("해당 코스를 스크랩에 추가하시겠습니까?")
                        .setPositiveButton("추가") { _, _ ->
                            recommendDetailViewModel.addMyScrap(trackBoardDto!!.trackBoardSeq, title)
                        }.show()
                } else { // 이미 스크랩되어 있을 때
                    dialog.setTitle("해당 코스를 스크랩에서 삭제하시겠습니까?")
                        .setPositiveButton("삭제") { _, _ ->
                            recommendDetailViewModel.deleteMyScrap(currentScrapSeq)
                        }.show()
                }
            }
        }
    }

    private fun initViewModelCallBack(){
       recommendDetailViewModel.successMsgEvent.observe(viewLifecycleOwner){
           isScrapped = !isScrapped
           if(isScrapped){
               binding.imageBookmark.setColorFilter(ContextCompat.getColor(requireContext(), R.color.main_purple))
           } else {
               binding.imageBookmark.colorFilter = null
           }
           showToast(it)
       }

        recommendDetailViewModel.errorMsgEvent.observe(viewLifecycleOwner){
            showToast(it)
        }

        recommendDetailViewModel.currentScrapSeq.observe(viewLifecycleOwner){
            currentScrapSeq = it
        }

        recommendDetailViewModel.isScrapped.observe(viewLifecycleOwner){
            if(it == 1){
                isScrapped = true
                binding.imageBookmark.setColorFilter(ContextCompat.getColor(requireContext(), R.color.main_purple))
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