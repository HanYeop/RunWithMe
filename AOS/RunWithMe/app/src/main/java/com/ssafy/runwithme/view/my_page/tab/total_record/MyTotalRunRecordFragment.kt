package com.ssafy.runwithme.view.my_page.tab.total_record

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.next
import com.kizitonwose.calendarview.utils.previous
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentMyTotalRunRecordBinding
import com.ssafy.runwithme.databinding.MyCalendarDayBinding
import com.ssafy.runwithme.databinding.MyCalendarHeaderBinding
import com.ssafy.runwithme.model.dto.RunRecordDto
import com.ssafy.runwithme.utils.Result
import com.ssafy.runwithme.utils.TAG
import com.ssafy.runwithme.view.my_page.MyPageFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

@AndroidEntryPoint
class MyTotalRunRecordFragment : BaseFragment<FragmentMyTotalRunRecordBinding>(R.layout.fragment_my_total_run_record) {

    private val myTotalRunRecordViewModel by viewModels<MyTotalRunRecordViewModel>()

    private lateinit var totalRunRecordList : List<RunRecordDto>
    private lateinit var dayRecord : Map<LocalDate, List<RunRecordDto>>
    private lateinit var myTotalRunRecordAdapter : MyTotalRunRecordAdapter

    private var selectedDate: LocalDate? = null
    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")
    private var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    // private var month : Int = LocalDate.now().monthValue
    // private var year : Int = LocalDate.now().year

    override fun init() {
        myTotalRunRecordViewModel.getMyTotalRecord()
        binding.totalRunVM = myTotalRunRecordViewModel

        myTotalRunRecordViewModel.getMyRunRecord()

        myTotalRunRecordAdapter = MyTotalRunRecordAdapter(myTotalRunRecordListener)

        binding.recyclerCalendar.apply {
            adapter = myTotalRunRecordAdapter
            addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
        }

        initViewModelCallBack()

        initClickListener()
    }

    private fun initClickListener(){
        binding.apply {
            tvPractice.setOnClickListener {
                findNavController().navigate(R.id.action_MyPageFragment_to_practiceListFragment)
            }
        }
    }

    private fun initViewModelCallBack(){
        myTotalRunRecordViewModel.errorMsgEvent.observe(viewLifecycleOwner){
            showToast(it)
        }

        lifecycleScope.launch {
            myTotalRunRecordViewModel.monthRunRecordList.collectLatest {
                if(it is Result.Success){
                    totalRunRecordList = it.data.data

                    dayRecord = totalRunRecordList.groupBy {
                        val date : LocalDateTime = LocalDateTime.parse(it.runRecordStartTime, formatter)
                        date.toLocalDate()
                    }
                    Log.d(TAG, "initCalendar: $dayRecord")
                    initCalendar()
                }
            }
        }
    }

    // 캘린더에 러닝 기록이 있는 날 색칠하기
    // private fun changeMonth(){}

    // 캘린더 처음부터 그리기
    private fun initCalendar(){
        val daysOfWeek = daysOfWeekFromLocale()
        val currentMonth = YearMonth.now()
        binding.calendar.setup(currentMonth.minusMonths(10), currentMonth.plusMonths(10), daysOfWeek.first())
        binding.calendar.scrollToMonth(currentMonth)

        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay // Will be set when this container is bound.
            val binding = MyCalendarDayBinding.bind(view)
            init {
                view.setOnClickListener {
                    if (day.owner == DayOwner.THIS_MONTH) {
                        if (selectedDate != day.date) {
                            val oldDate = selectedDate
                            selectedDate = day.date
                            val binding = this@MyTotalRunRecordFragment.binding
                            binding.calendar.notifyDateChanged(day.date)
                            oldDate?.let { binding.calendar.notifyDateChanged(it) }
                            updateAdapterForDate(day.date)
                        }
                    }
                }
            }
        }

        binding.calendar.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.binding.tvCalendarDay
                val layout = container.binding.layoutCalendarDay
                textView.text = day.date.dayOfMonth.toString()

                val runView = container.binding.viewRunDay

                if (day.owner == DayOwner.THIS_MONTH) {
                    textView.setTextColorRes(R.color.black_high_emphasis)
                    layout.setBackgroundResource(if (selectedDate == day.date) R.drawable.calendar_selected_bg else 0)

                    val dayRecord = dayRecord[day.date]
                    if (dayRecord != null) { // 러닝 기록이 있는 날은 색칠
                        runView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.main_blue_green))
                        // 러닝 중에서 1번이라도 목표 달성했을 시 색변경
                        for(item in dayRecord){
                            if(item.runRecordRunningCompleteYN == "Y"){
                                runView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.main_orange))
                                break
                            }
                        }
                    }
                } else {
                    textView.setTextColorRes(R.color.light_grey)
                    layout.background = null
                }
            }
        }

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val legendLayout = MyCalendarHeaderBinding.bind(view).legendLayout
        }

        binding.calendar.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                // Setup each header day text if we have not done that already.
                if (container.legendLayout.tag == null) {
                    container.legendLayout.tag = month.yearMonth
                    container.legendLayout.children.map { it as TextView }.forEachIndexed { index, tv ->
                        tv.text = daysOfWeek[index].getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                            .toUpperCase(Locale.ENGLISH)
                        tv.setTextColorRes(R.color.black)
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                    }
                    month.yearMonth
                }
            }
        }

        binding.calendar.monthScrollListener = { month ->
            val title = "${month.yearMonth.year}년 ${monthTitleFormatter.format(month.yearMonth)}"
            binding.tvYearMonth.text = title

            selectedDate?.let {
                // Clear selection if we scroll to a new month.
                selectedDate = null
                binding.calendar.notifyDateChanged(it)
                updateAdapterForDate(null)
            }
        }

        // 다음 달
        binding.imageNextMonth.setOnClickListener {
            binding.calendar.findFirstVisibleMonth()?.let {
                binding.calendar.smoothScrollToMonth(it.yearMonth.next)
            }
        }
        // 이전 달
        binding.imagePreviousMonth.setOnClickListener {
            binding.calendar.findFirstVisibleMonth()?.let {
                binding.calendar.smoothScrollToMonth(it.yearMonth.previous)
            }
        }
    }

    private fun updateAdapterForDate(date: LocalDate?) {
        myTotalRunRecordAdapter.submitList(dayRecord[date].orEmpty())
    }

    private val myTotalRunRecordListener : MyTotalRunRecordListener = object : MyTotalRunRecordListener {
        override fun onItemClick(runRecordDto: RunRecordDto) {
            val action = MyPageFragmentDirections.actionMyPageFragmentToRunRecordDetailFragment(runRecordDto)
            findNavController().navigate(action)
        }
    }

}