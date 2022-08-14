package com.ssafy.runwithme.view.crew_detail

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.mikephil.charting.components.*
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentCrewDetailBinding
import com.ssafy.runwithme.model.dto.CrewDto
import com.ssafy.runwithme.model.dto.ImageFileDto
import com.ssafy.runwithme.model.response.MyGraphDataResponse
import com.ssafy.runwithme.utils.Result
import com.ssafy.runwithme.utils.TAG
import com.ssafy.runwithme.utils.USER
import com.ssafy.runwithme.utils.runningStart
import com.ssafy.runwithme.view.running.RunningActivity
import com.ssafy.runwithme.view.running.RunningViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.lang.Math.round
import javax.inject.Inject

@AndroidEntryPoint
class CrewDetailFragment : BaseFragment<FragmentCrewDetailBinding>(R.layout.fragment_crew_detail) {

    @Inject
    lateinit var sharedPref: SharedPreferences
    private val args by navArgs<CrewDetailFragmentArgs>()
    private lateinit var crewDto: CrewDto
    private lateinit var imageFileDto: ImageFileDto
    private val crewDetailViewModel by viewModels<CrewDetailViewModel>()
    private val runningViewModel by viewModels<RunningViewModel>()
    private var mySeq: Int = 0

    override fun init() {

        binding.apply {
            crewDetailVM = crewDetailViewModel
        }

        initClickListener()

        mySeq = sharedPref.getString(USER, "0")!!.toInt()
        crewDto = args.crewdto
        imageFileDto = args.imagefiledto

        binding.crewDto = crewDto
        binding.imageFileDto = imageFileDto

        initViewModelCallback()

        initBarChart()

        runningViewModel.getMyProfile()
    }

    private fun initBarChart() {
        // 차트 회색 배경 설정 (default = false)
        binding.apply {
            chartMyRecord.setDrawGridBackground(false)
            // 막대 그림자 설정 (default = false)
            chartMyRecord.setDrawBarShadow(false)
            // 차트 테두리 설정 (default = false)
            chartMyRecord.setDrawBorders(true)

            //핀치 줌
            chartMyRecord.setPinchZoom(true)

            chartMyRecord.setNoDataText("기록이 없습니다.")

            val description = Description()
            // 오른쪽 하단 모서리 설명 레이블 텍스트 표시 (default = false)
            description.isEnabled = false
            chartMyRecord.description = description

            // X, Y 바의 애니메이션 효과
            chartMyRecord.animateY(1000)
            chartMyRecord.animateX(1000)

            // 바텀 좌표 값
            val xAxis: XAxis = chartMyRecord.xAxis
            // x축 위치 설정
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            // 그리드 선 수평 거리 설정
            xAxis.granularity = 1f
            // x축 텍스트 컬러 설정
            xAxis.textColor = Color.BLACK
            // x축 선 설정 (default = true)
            xAxis.setDrawAxisLine(false)
            // 격자선 설정 (default = true)
            xAxis.setDrawGridLines(false)

            val leftAxis: YAxis = chartMyRecord.axisLeft
            // 좌측 선 설정 (default = true)
            leftAxis.setDrawAxisLine(false)
            // 좌측 텍스트 컬러 설정
            leftAxis.textColor = Color.BLACK

            val rightAxis: YAxis = chartMyRecord.axisRight
            // 우측 선 설정 (default = true)
            rightAxis.setDrawAxisLine(false)
            // 우측 텍스트 컬러 설정
            rightAxis.textColor = Color.WHITE

            // 바차트의 타이틀
            val legend: Legend = chartMyRecord.legend
            // 범례 모양 설정 (default = 정사각형)
            legend.form = Legend.LegendForm.LINE
            // 타이틀 텍스트 사이즈 설정
            legend.textSize = 20f
            // 타이틀 텍스트 컬러 설정
            legend.textColor = Color.BLACK
            // 범례 위치 설정
            legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            // 범례 방향 설정
            legend.orientation = Legend.LegendOrientation.HORIZONTAL
            // 차트 내부 범례 위치하게 함 (default = false)
            legend.setDrawInside(false)
        }

    }


    private fun initClickListener() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            cardviewMyRecord.setOnClickListener {
                val action = CrewDetailFragmentDirections.actionCrewDetailFragmentToCrewMyRunRecordFragment(crewDto!!.crewSeq, crewDetailVM!!.myTotalRecordData.value)
                findNavController().navigate(action)
            }

            layoutRanking.setOnClickListener {
                val action = CrewDetailFragmentDirections.actionCrewDetailFragmentToCrewUserRankingFragment(crewDto!!)
                findNavController().navigate(action)
            }

            layoutBoard.setOnClickListener {
                val action =
                    CrewDetailFragmentDirections.actionCrewDetailFragmentToCrewBoardFragment(crewDto!!.crewSeq)
                findNavController().navigate(action)
            }

            layoutRecord.setOnClickListener {
                val action = CrewDetailFragmentDirections.actionCrewDetailFragmentToCrewUserRunRecordFragment(crewDto!!.crewSeq)
                findNavController().navigate(action)
            }

            btnJoinCrew.setOnClickListener {
                if(crewDto!!.crewPassword != null && crewDto!!.crewPassword != ""){
                    initConfirmJoinPasswdDialog()
                }else {
                    initConfirmJoinDialog()
                }
            }

            btnResignCrew.setOnClickListener {
                if(crewDetailViewModel.isCrewManager.value){
                    initConfirmDeleteDialog()
                }else{
                    initConfirmResignDialog()
                }
            }

            btnRunning.setOnClickListener {
                runningViewModel.runAbleToday(crewDto!!.crewSeq)
            }
        }
    }

    private fun initConfirmJoinDialog(){
        val text = "참가비 ${crewDto.crewCost}P가 지출됩니다.\n가입하시겠습니까?"
        val confirmJoinDialog = ConfirmJoinDialog(requireContext(), text, confirmJoinListener)
        confirmJoinDialog.show()
    }

    private fun initConfirmJoinPasswdDialog(){
        val text = "참가비 ${crewDto.crewCost}P가 지출됩니다.\n가입하시겠습니까?"
        val confirmJoinPasswdDialog = ConfirmJoinPasswdDialog(requireContext(), text, confirmJoinPasswdListener)
        confirmJoinPasswdDialog.show()
    }

    private fun initConfirmResignDialog(){
        val text = "탈퇴하시면 ${crewDto.crewCost}P를 돌려받을 수 있습니다.\n탈퇴하시겠습니까?"
        val confirmResignDialog = ConfirmResignDialog(requireContext(), text, confirmResignListener)
        confirmResignDialog.show()
    }

    private fun initConfirmDeleteDialog(){
        val text = "해체하시면 ${crewDto.crewCost}P를 돌려받을 수 있지만\n챌린지 크루가 사라집니다.\n해체하시겠습니까?"
        val confirmDeleteDialog = ConfirmResignDialog(requireContext(), text, confirmDeleteListener)
        confirmDeleteDialog.show()
    }

    private val confirmJoinListener : ConfirmJoinListener = object :  ConfirmJoinListener {
        override fun onItemClick() {
            crewDetailViewModel.joinCrew(crewDto!!.crewSeq, null)
        }
    }

    private val confirmJoinPasswdListener : ConfirmJoinPasswdListener = object : ConfirmJoinPasswdListener {
        override fun onItemClick(passwd: String) {
            crewDetailViewModel.joinCrew(crewDto!!.crewSeq, passwd)
        }
    }

    private val confirmDeleteListener : ConfirmResignListener = object : ConfirmResignListener {
        override fun onItemClick() {
            crewDetailViewModel.deleteCrew(crewDto!!.crewSeq)
            findNavController().popBackStack()
            findNavController().popBackStack()
        }
    }

    private val confirmResignListener : ConfirmResignListener = object : ConfirmResignListener {
        override fun onItemClick() {
            crewDetailViewModel.resignCrew(crewDto!!.crewSeq)
            findNavController().popBackStack()
            findNavController().popBackStack()
        }
    }

    private fun btnRunningClick(){
    }

    private fun initViewModelCallback() {
        crewDetailViewModel.setState(crewDto.crewDateStart, crewDto.crewDateEnd, crewDto.crewTimeStart, crewDto.crewTimeEnd)

        crewDetailViewModel.checkCrewMember(crewDto.crewSeq)

        crewDetailViewModel.getMyGraphData(crewDto.crewSeq, crewDto.crewGoalType)

        crewDetailViewModel.getTotalRecordData(crewDto.crewSeq)

        crewDetailViewModel.checkCrewManager(crewDto.crewManagerSeq == mySeq)

        crewDetailViewModel.successMsgEvent.observe(viewLifecycleOwner) {
            showToast(it)
            binding.apply {
                btnJoinCrew.visibility = View.GONE
                btnResignCrew.visibility = View.VISIBLE
            }
        }

        crewDetailViewModel.errorMsgEvent.observe(viewLifecycleOwner) {
            showToast(it)
        }

        lifecycleScope.launch {
            crewDetailViewModel.myGraphData.collectLatest {
                if(it is Result.Success){
                    setData(it.data.data)
                }
            }
        }

    }

    private fun setData(myGraphDataList : List<MyGraphDataResponse>) {
        binding.apply {
            // Zoom In / Out 가능 여부 설정
            chartMyRecord.setScaleEnabled(true)

            val distanceList = arrayListOf<Double>()
            val timeList = arrayListOf<Int>()
            val dateList = arrayListOf<String>()

            val type = crewDto!!.crewGoalType

            var title = "거리"
            if(type == "time"){
                title = "시간"
            }

            Log.d(TAG, "setData: $myGraphDataList")
            
            for(myGraphData in myGraphDataList){
                if(type == "time"){
                    timeList.add(myGraphData.amount / 60)
                }else{
                    distanceList.add(round(1.0 * myGraphData.amount / 1000.0 * 100)/ 100.0)
                }

                dateList.add("${myGraphData.month}-${myGraphData.day}")
            }

            val entries = arrayListOf<BarEntry>()

            if(type == "time"){
                for(i in 0..(timeList.size - 1)){
                    entries.add(BarEntry(1F * i, 1F * timeList.get(i)))
                }
            }else{
                for(i in 0..(distanceList.size - 1)){
                    entries.add(BarEntry(1F * i, distanceList.get(i).toFloat()))
                }
            }


            val barDataSet = BarDataSet(entries, title)
            // 바 색상 설정 (ColorTemplate.LIBERTY_COLORS)
            barDataSet.setColors(
                R.color.main_blue
            )
//            barDataSet.isHighlightEnabled = false


            val data = BarData(barDataSet)
            Log.d(TAG, "setData: dateList : $dateList")
            chartMyRecord.xAxis.valueFormatter = MyAxisFormatter(dateList)
            //chartMyRecord.xAxis.valueFormatter = IndexAxisValueFormatter(dateList)
            chartMyRecord.data = data
            chartMyRecord.invalidate()

            chartMyRecord.axisLeft.run {
                axisMinimum = 0F
            }

        }

        runningViewModel.errorMsgEvent.observe(this){
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        runningViewModel.runAbleEvent.observe(this){
            runningStart(sharedPref, crewDto.crewSeq, crewDto.crewName
                ,crewDto.crewGoalType, crewDto.crewGoalAmount, 0)
            findNavController().popBackStack()
            findNavController().popBackStack()
            startActivity(Intent(requireContext(), RunningActivity::class.java))
        }
    }

    inner class MyAxisFormatter(val dateList: ArrayList<String>) : ValueFormatter() {
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return dateList.getOrNull(value.toInt()) ?: value.toString()
        }
    }

}