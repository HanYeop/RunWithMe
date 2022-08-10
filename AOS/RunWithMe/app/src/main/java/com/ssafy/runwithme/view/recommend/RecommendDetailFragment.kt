package com.ssafy.runwithme.view.recommend

import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
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

    private val recommendDetailViewModel by activityViewModels<RecommendDetailViewModel>()

    private var isScrapped = false
    private var distanceText = ""
    private var timeText = ""
    private var currentScrapSeq = 0

    override fun init() {
        runRecordDto = args.runRecordDto
        trackBoardDto = args.trackBoardDto
        binding.imgSeq = args.imgSeq
        binding.content = trackBoardDto!!.content

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
                if(!isScrapped){ // 스크랩 안 되어 있을 때
                    ScrapDialog(requireContext(), listener, true).show()
                } else { // 이미 스크랩되어 있을 때
                    ScrapDialog(requireContext(), listener, false).show()
                }
            }

            btnRoute.setOnClickListener {
                val action = RecommendDetailFragmentDirections.actionRecommendDetailFragmentToRunningRouteFragment(
                    runRecordDto!!.runRecordSeq,distanceText,timeText)
                findNavController().navigate(action)
            }
        }
    }

    private val listener : ScrapDialogListener = object : ScrapDialogListener {
        override fun onItemAdd(title: String) {
            recommendDetailViewModel.addMyScrap(trackBoardDto!!.trackBoardSeq, title)
        }
        override fun onItemDelete() {
            recommendDetailViewModel.deleteMyScrap(currentScrapSeq)
        }
    }

    private fun initViewModelCallBack(){
       recommendDetailViewModel.successMsgEvent.observe(viewLifecycleOwner){
           isScrapped = !isScrapped
           if(isScrapped){
               binding.imageBookmark.setColorFilter(ContextCompat.getColor(requireContext(), R.color.main_orange))
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
                binding.imageBookmark.setColorFilter(ContextCompat.getColor(requireContext(), R.color.main_orange))
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