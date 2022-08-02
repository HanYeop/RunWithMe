package com.ssafy.runwithme.view.my_page.tab.total_record

import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentMyTotalRunRecordBinding
import dagger.hilt.android.AndroidEntryPoint
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.ssafy.runwithme.databinding.MyCalendarDayBinding
import com.ssafy.runwithme.databinding.MyCalendarHeaderBinding
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*
import androidx.core.view.children
import com.kizitonwose.calendarview.utils.next
import com.kizitonwose.calendarview.utils.previous

@AndroidEntryPoint
class MyTotalRunRecordFragment : BaseFragment<FragmentMyTotalRunRecordBinding>(R.layout.fragment_my_total_run_record) {

    private val myTotalRunRecordViewModel by viewModels<MyTotalRunRecordViewModel>()
    private var selectedDate: LocalDate? = null
    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")

    override fun init() {
        myTotalRunRecordViewModel.getMyTotalRecord()
        binding.totalRunVM = myTotalRunRecordViewModel

        initViewModelCallBack()

        initCalendar()
    }

    private fun initViewModelCallBack(){
        myTotalRunRecordViewModel.errorMsgEvent.observe(viewLifecycleOwner){
            showToast(it)
        }
    }

    private fun initCalendar(){
        binding.recyclerCalendar.apply {
            addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
        }

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
                            // updateAdapterForDate(day.date)
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

                val firstView = container.binding.viewRunRecordFirst
                val secView = container.binding.viewRunRecordSecond
                firstView.background = null
                secView.background = null

                if (day.owner == DayOwner.THIS_MONTH) {
                    textView.setTextColorRes(R.color.black_high_emphasis)
                    layout.setBackgroundResource(if (selectedDate == day.date) R.drawable.calendar_selected_bg else 0)

                    // val flights = flights[day.date]
//                    if (flights != null) {
//                        if (flights.count() == 1) {
//                            flightBottomView.setBackgroundColor(view.context.getColorCompat(flights[0].color))
//                        } else {
//                            flightTopView.setBackgroundColor(view.context.getColorCompat(flights[0].color))
//                            flightBottomView.setBackgroundColor(view.context.getColorCompat(flights[1].color))
//                        }
//                    }
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
            val title = "${monthTitleFormatter.format(month.yearMonth)} ${month.yearMonth.year}"
            binding.tvYearMonth.text = title

            selectedDate?.let {
                // Clear selection if we scroll to a new month.
                selectedDate = null
                binding.calendar.notifyDateChanged(it)
                // updateAdapterForDate(null)
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

}