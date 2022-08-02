package com.ssafy.runwithme.view.crew_detail

import android.graphics.Color
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentCrewDetailBinding
import com.ssafy.runwithme.model.dto.CrewDto
import com.ssafy.runwithme.model.dto.ImageFileDto
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CrewDetailFragment : BaseFragment<FragmentCrewDetailBinding>(R.layout.fragment_crew_detail) {

    private val args by navArgs<CrewDetailFragmentArgs>()
    private lateinit var crewDto: CrewDto
    private lateinit var imageFileDto: ImageFileDto
    private val crewDetailViewModel by viewModels<CrewDetailViewModel>()

    override fun init() {

        binding.apply {
            crewDetailVM = crewDetailViewModel

        }

        initClickListener()

        crewDto = args.crewdto
        imageFileDto = args.imagefiledto

        binding.crewDto = crewDto
        binding.imageFileDto = imageFileDto

        initViewModelCallback()

        initBarChart()
        setData()
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

    private fun setData() {
        binding.apply {
            // Zoom In / Out 가능 여부 설정
            chartMyRecord.setScaleEnabled(true)

            val valueList = ArrayList<BarEntry>()
            val title = "거리"

            // 임의 데이터
            for (i in 0 until 20) {
                valueList.add(BarEntry(i.toFloat(), i * 100f))
            }

            val barDataSet = BarDataSet(valueList, title)
            // 바 색상 설정 (ColorTemplate.LIBERTY_COLORS)
            barDataSet.setColors(
                R.color.main_purple
            )
//            barDataSet.isHighlightEnabled = false

            val data = BarData(barDataSet)
            chartMyRecord.data = data
            chartMyRecord.invalidate()
        }

    }

    private fun initClickListener() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            cardviewMyRecord.setOnClickListener {
                val action = CrewDetailFragmentDirections.actionCrewDetailFragmentToCrewMyRunRecordFragment(crewDto!!.crewSeq)
                findNavController().navigate(action)
            }

            layoutRanking.setOnClickListener {
                val action = CrewDetailFragmentDirections.actionCrewDetailFragmentToCrewUserRankingFragment(crewDto!!.crewSeq)
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
                crewDetailViewModel.joinCrew(crewDto!!.crewSeq, null)
            }
        }
    }

    private fun initViewModelCallback() {
        crewDetailViewModel.getState(crewDto.crewDateStart, crewDto.crewDateEnd)

        crewDetailViewModel.checkCrewMember(crewDto.crewSeq)

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



    }

}