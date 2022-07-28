package com.ssafy.runwithme.view.crew_recruit.create

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.ssafy.runwithme.model.dto.CrewDto
import com.ssafy.runwithme.repository.CrewManagerRepository
import com.ssafy.runwithme.utils.Result
import com.ssafy.runwithme.utils.SingleLiveEvent
import com.ssafy.runwithme.utils.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.time.LocalDate
import java.util.*
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class CreateCrewViewModel @Inject constructor(
    private val crewManagerRepository: CrewManagerRepository
) : ViewModel(){

    private val _successMsgEvent = SingleLiveEvent<String>()
    val successMsgEvent get() = _successMsgEvent

    private val _errorMsgEvent = SingleLiveEvent<String>()
    val errorMsgEvent get() = _errorMsgEvent

    private val _failMsgEvent = SingleLiveEvent<String>()
    val failMsgEvent get() = _failMsgEvent

    val crewName : MutableStateFlow<String> = MutableStateFlow("")

    val crewDescription : MutableStateFlow<String> = MutableStateFlow("안녕하세요. 런윗미 크루입니다.\n함께 같이 달려보아요")

    private val _goalWeeks : MutableStateFlow<Int> = MutableStateFlow(1)
    val goalWeeks get() = _goalWeeks.asStateFlow()

    private val _dateStart : MutableStateFlow<String> = MutableStateFlow("")
    val dateStart get() = _dateStart.asStateFlow()

    private val _dateEnd : MutableStateFlow<String> = MutableStateFlow("")
    val dateEnd get() = _dateEnd.asStateFlow()

    private val _timeStart : MutableStateFlow<String> = MutableStateFlow("20:00")
    val timeStart get() = _timeStart.asStateFlow()

    private val _timeEnd : MutableStateFlow<String> = MutableStateFlow("21:00")
    val timeEnd get() = _timeEnd.asStateFlow()

    private val _maxMemeber : MutableStateFlow<String> = MutableStateFlow("7")
    val maxMember get() = _maxMemeber.asStateFlow()

    private val _cost : MutableStateFlow<String> = MutableStateFlow("10000")
    val cost get() = _cost.asStateFlow()

    private val _time : MutableStateFlow<String> = MutableStateFlow("30")
    val time get() = _time.asStateFlow()

    private val _distance : MutableStateFlow<String> = MutableStateFlow("3")
    val distance get() = _distance.asStateFlow()

    private val _goalTypeDistance : MutableStateFlow<Boolean> = MutableStateFlow(true)
    val goalTypeDistance get() = _goalTypeDistance.asStateFlow()

    private val _goalDays : MutableStateFlow<String> = MutableStateFlow("3")
    val goalDays get() = _goalDays.asStateFlow()

    private var passwd : String = ""


    fun createCrew(imgFile : MultipartBody.Part?) {
        var goalType = ""
        var goalAmount = 0
        if(goalTypeDistance.value){
            goalType = "distance"
            goalAmount = distance.value.toInt() * 1000
        }else{
            goalType = "time"
            goalAmount = time.value.toInt() * 60
        }

        var pass : String? = null
        if(!passwd.equals("")){
            pass = passwd
        }

//        val crew = CrewDto(0, crewName.value, crewDescription.value,
//                        goalDays.value.toInt(), goalType, goalAmount,
//            dateStart.value + " 00:00:00", dateEnd.value + " 23:59:59",
//            timeStart.value + ":00", timeEnd.value + ":00", pass,
//                        cost.value.toInt(), maxMember.value.toInt(), "", 0)

        val crew = CrewDto(0, crewName.value, crewDescription.value,
            goalDays.value.toInt(), goalType, goalAmount,
            dateStart.value + " 00:00:00", dateEnd.value + " 23:59:59",
            timeStart.value + ":00", timeEnd.value + ":00", pass,
            0, 1, maxMember.value.toInt(), "", 0)

        val json = Gson().toJson(crew)
        val crewDto = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), json)

        Log.d(TAG, "createCrew: json $json")

        viewModelScope.launch(Dispatchers.IO) {
            crewManagerRepository.createCrew(crewDto, imgFile).collectLatest {
                if(it is Result.Success){
                    _successMsgEvent.postValue(it.data.msg)
                }else if(it is Result.Error){
                    Log.d(TAG, "createCrew viewmodel: ${it.exception}")
                    _errorMsgEvent.postValue("오류가 발생했습니다.")
                }else if(it is Result.Fail){
                    _failMsgEvent.postValue(it.data.msg)
                }
            }
        }
    }

    fun setGoalTypeDistance(isRight : Boolean){
        if(isRight){
            _goalTypeDistance.value = true
        }else{
            _goalTypeDistance.value = false
        }
    }

    fun setPasswd(passwd : String){
        Log.d(TAG, "setPasswd: ${passwd.length}")
        if(passwd.length != 4 && passwd.isNotEmpty()){
            this.passwd = ""
            _failMsgEvent.postValue("비밀번호는 4자리입니다.")
        }else {
            this.passwd = passwd
        }
    }

    fun setGoalDays(days: Int){
        _goalDays.value = days.toString()
    }

    fun setTime(time : String){
        _time.value = time
    }

    fun setDistance(distance : Int){
        _distance.value = distance.toString()
    }

    fun setGoalWeeks(num : Int) {
        _goalWeeks.value = num
    }

    fun setMaxMember(max : Int){
        _maxMemeber.value = max.toString()
    }

    fun setCost(cost: String){
        _cost.value = cost
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
        _dateStart.value = date
    }

    fun setDateEnd(year : Int, month : Int, dayOfMonth : Int){
        val clickDate = LocalDate.of(year, month, dayOfMonth)
        val endDate = clickDate.plusWeeks(goalWeeks.value.toLong()).minusDays(1)
        val endDateYear = endDate.year
        val endDateMonthInt = endDate.monthValue
        var endDateMonth = endDateMonthInt.toString()
        if(endDateMonthInt < 10){
            endDateMonth = "0" + endDateMonth
        }
        val endDateDayInt = endDate.dayOfMonth
        var endDateDay = endDateDayInt.toString()
        if(endDateDayInt < 10){
            endDateDay = "0" + endDateDay
        }
        _dateEnd.value = "$endDateYear-$endDateMonth-$endDateDay"
    }

    fun initDate() {
        val now = LocalDate.now()
        val startDateYear = now.year
        val startDateMonthInt = now.monthValue
        var startDateMonth = startDateMonthInt.toString()
        if(startDateMonthInt < 10){
            startDateMonth = "0" + startDateMonth
        }
        val startDateDayInt = now.dayOfMonth + 1
        var startDateDay = startDateDayInt.toString()
        if(startDateDayInt < 10){
            startDateDay = "0" + startDateDay
        }
        setDateStart("$startDateYear-${startDateMonth}-$startDateDay")
        setDateEnd(startDateYear, startDateMonthInt, startDateDayInt)
    }

    fun updateDateEnd(){
        val dateToken = StringTokenizer(dateStart.value, "-")
        val date = LocalDate.of(dateToken.nextToken().toInt(), dateToken.nextToken().toInt(), dateToken.nextToken().toInt())
        val endDate = date.plusWeeks(goalWeeks.value.toLong()).minusDays(1)
        val endDateYear = endDate.year
        val endDateMonthInt = endDate.monthValue
        var endDateMonth = endDateMonthInt.toString()
        if(endDateMonthInt < 10){
            endDateMonth = "0" + endDateMonth
        }
        val endDateDayInt = endDate.dayOfMonth
        var endDateDay = endDateDayInt.toString()
        if(endDateDayInt < 10){
            endDateDay = "0" + endDateDay
        }
        _dateEnd.value = "$endDateYear-$endDateMonth-$endDateDay"
    }
}