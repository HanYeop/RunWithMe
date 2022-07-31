package com.ssafy.runwithme.view.crew_recruit.search

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.ssafy.runwithme.repository.CrewManagerRepository
import com.ssafy.runwithme.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.util.*
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
class SearchCrewViewModel : ViewModel(){

    private val _failMsgEvent = SingleLiveEvent<String>()
    val failMsgEvent get() = _failMsgEvent

    val crewName : MutableStateFlow<String> = MutableStateFlow("")

    private val _dateStart : MutableStateFlow<String> = MutableStateFlow("2022-")
    val dateStart get() = _dateStart.asStateFlow()

    private val _dateEnd : MutableStateFlow<String> = MutableStateFlow("")
    val dateEnd get() = _dateEnd.asStateFlow()

    private val _timeStart : MutableStateFlow<String> = MutableStateFlow("20:00")
    val timeStart get() = _timeStart.asStateFlow()

    private val _timeEnd : MutableStateFlow<String> = MutableStateFlow("21:00")
    val timeEnd get() = _timeEnd.asStateFlow()

    private val _minCost : MutableStateFlow<String> = MutableStateFlow("10000")
    val minCost get() = _minCost.asStateFlow()

    private val _maxCost : MutableStateFlow<String> = MutableStateFlow("30000")
    val maxCost get() = _maxCost.asStateFlow()

    private val _minTime : MutableStateFlow<String> = MutableStateFlow("30")
    val minTime get() = _minTime.asStateFlow()

    private val _maxTime : MutableStateFlow<String> = MutableStateFlow("600")
    val maxTime get() = _maxTime.asStateFlow()

    private val _minDistance : MutableStateFlow<String> = MutableStateFlow("3")
    val minDistance get() = _minDistance.asStateFlow()

    private val _maxDistance : MutableStateFlow<String> = MutableStateFlow("60")
    val maxDistance get() = _maxDistance.asStateFlow()

    private val _goalTypeDistance : MutableStateFlow<Boolean> = MutableStateFlow(true)
    val goalTypeDistance get() = _goalTypeDistance.asStateFlow()

    private val _minGoalDays : MutableStateFlow<String> = MutableStateFlow("1")
    val minGoalDays get() = _minGoalDays.asStateFlow()

    private val _maxGoalDays : MutableStateFlow<String> = MutableStateFlow("7")
    val maxGoalDays get() = _maxGoalDays.asStateFlow()

    fun setGoalTypeDistance(isRight : Boolean){
        if(isRight){
            _goalTypeDistance.value = true
        }else{
            _goalTypeDistance.value = false
        }
    }

    fun setMinGoalDays(days: Int){
        if(maxGoalDays.value.toInt() < days){
            _maxGoalDays.value = "7"
        }

        _minGoalDays.value = days.toString()
    }

    fun setMaxGoalDays(days: Int){
        _maxGoalDays.value = days.toString()
    }

    fun setMinTime(time : String){
        if(maxTime.value.toInt() < time.toInt()){
            _maxTime.value = (time.toInt() + 10).toString()
        }

        _minTime.value = time
    }

    fun setMaxTime(time : String){
        if(minTime.value.toInt() > time.toInt()){
            _maxTime.value = (minTime.value.toInt() + 10).toString()
            _failMsgEvent.postValue("시간을 재설정해주세요.")
        }else {
            _maxTime.value = time
        }
    }

    fun setMinDistance(distance : Int){
        if(maxDistance.value.toInt() < distance){
            _maxDistance.value = (distance + 1).toString()
        }
        _minDistance.value = distance.toString()
    }

    fun setMaxDistance(distance : Int){
        if(minDistance.value.toInt() > distance){
            _maxDistance.value = (minDistance.value.toInt() + 1).toString()
            _failMsgEvent.postValue("거리를 재설정해주세요.")
        }else {
            _maxDistance.value = distance.toString()
        }
    }

    fun setMinCost(cost: String){
        if(maxCost.value < cost){
            _maxCost.value = (cost.toInt() + 5000).toString()
        }
        _minCost.value = cost

    }

    fun setMaxCost(cost: String){
        if(minCost.value.toInt() > cost.toInt()){
            _maxCost.value = (minCost.value.toInt() + 5000).toString()
            _failMsgEvent.postValue("참가비 범위를 다시 설정해주세요.")
        }else {
            _maxCost.value = cost
        }
    }

    fun setTimeStart(hour: String, minute: String){
        _timeStart.value = "$hour:$minute"

        val timeToken = StringTokenizer(timeEnd.value, ":")
        val endHour = timeToken.nextToken().toInt()
        val endMinuteInt = timeToken.nextToken().toInt()

        var isRightTime = true
        val startHourInt = hour.toInt()
        val startMinuteInt = minute.toInt()

        if(startHourInt == endHour){

            if(startMinuteInt >= endMinuteInt){
                isRightTime = false
            }

        }else if(startHourInt > endHour){
            isRightTime = false
        }

        var endMinute = endMinuteInt.toString()
        if(endMinuteInt < 10){
            endMinute = "0" + endMinute
        }

        if (isRightTime){
            _timeEnd.value = "$endHour:$endMinute"
        }else {
            if (startHourInt == 23) {
                _timeEnd.value = "$startHourInt:30"
            } else {
                _timeEnd.value = "${startHourInt + 1}:30"
                _failMsgEvent.postValue("시간을 다시 설정해주세요.")
            }
        }
    }

    fun setTimeEnd(endHour: String, endMinute: String){
        val timeToken = StringTokenizer(timeStart.value, ":")
        val startHour = timeToken.nextToken().toInt()
        val startMinute = timeToken.nextToken().toInt()

        var isRightTime = true
        val endHourInt = endHour.toInt()
        val endMinuteInt = endMinute.toInt()

        if(startHour == endHourInt){

            if(startMinute >= endMinuteInt){
                isRightTime = false
            }

        }else if(startHour > endHourInt){
            isRightTime = false
        }

        if (isRightTime){
            _timeEnd.value = "$endHour:$endMinute"
        }else{
            if(startHour == 23){
                _timeEnd.value = "23:30"
            }else{
                _timeEnd.value = "${startHour + 1}:30"
            }
            _failMsgEvent.postValue("시간을 다시 설정해주세요.")
        }
        //_timeEnd.value = "$endHour:$endMinute"
    }

    fun setDateStart(date : String) {
        val startToken = StringTokenizer(date, "-")
        var startYear = startToken.nextToken()
        var startMonth = startToken.nextToken()
        var startDay = startToken.nextToken()
        var startYearInt = startYear.toInt()
        var startMonthInt = startMonth.toInt()
        var startDayInt = startDay.toInt()

        val endToken = StringTokenizer(dateEnd.value, "-")
        var endYear = endToken.nextToken()
        var endMonth = endToken.nextToken()
        var endDay = endToken.nextToken()
        var endYearInt = endYear.toInt()
        var endMonthInt = endMonth.toInt()
        var endDayInt = endDay.toInt()

        if(startYearInt == endYearInt){

            if(startMonthInt == endMonthInt){

                if(startDayInt < endDayInt){

                }else{
                    endDayInt = startDayInt + 1
                    endDay = endDayInt.toString()
                    if(endDayInt < 10){
                        endDay = "0" + endDay
                    }
                }

            }else if(startMonthInt > endMonthInt){
                if(startMonthInt == 12){
                    endMonthInt = 1
                    endYear = (startYearInt + 1).toString()
                }else {
                    endMonthInt = startMonthInt + 1
                }
                endMonth = endMonthInt.toString()
                if(endMonthInt < 10){
                    endMonth = "0" + endMonth
                }

            }

        }else if(startYearInt > endYearInt){
            endYear = (startYearInt + 1).toString()

        }

        _dateStart.value = date
        _dateEnd.value = "$endYear-$endMonth-$endDay"
    }

    fun setDateEnd(date: String){
        _dateEnd.value = date
    }

    fun initDate() {
        val now = LocalDate.now()
        now.plusDays(1)
        val startDateYearInt = now.year
        val startDateMonthInt = now.monthValue
        var startDateMonth = startDateMonthInt.toString()
        if(startDateMonthInt < 10){
            startDateMonth = "0" + startDateMonth
        }
        val startDateDayInt = now.dayOfMonth
        var startDateDay = startDateDayInt.toString()
        if(startDateDayInt < 10){
            startDateDay = "0" + startDateDay
        }

        val end = now.plusWeeks(1)

        val endYear = end.year
        val endMonthInt = end.monthValue
        var endMonth = endMonthInt.toString()
        if(endMonthInt < 10){
            endMonth = "0" + endMonth
        }

        val endDayInt = end.dayOfMonth
        var endDay = endDayInt.toString()
        if(endDayInt < 10){
            endDay = "0" + endDay
        }

        _dateStart.value = "$startDateYearInt-${startDateMonth}-$startDateDay"
        _dateEnd.value = "$endYear-${endMonth}-$endDay"
    }


}