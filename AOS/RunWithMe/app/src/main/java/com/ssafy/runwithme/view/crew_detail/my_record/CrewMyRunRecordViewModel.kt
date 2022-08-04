package com.ssafy.runwithme.view.crew_detail.my_record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.dto.RunRecordDto
import com.ssafy.runwithme.model.response.CreateRunRecordResponse
import com.ssafy.runwithme.model.response.CrewMyTotalRecordDataResponse
import com.ssafy.runwithme.repository.CrewActivityRepository
import com.ssafy.runwithme.repository.MyActivityRepository
import com.ssafy.runwithme.utils.Result
import com.ssafy.runwithme.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
class CrewMyRunRecordViewModel @Inject constructor(
    private val crewActivityRepository: CrewActivityRepository
) : ViewModel() {

    private val _monthRunRecordList: MutableStateFlow<Result<BaseResponse<List<RunRecordDto>>>>
            = MutableStateFlow(Result.Uninitialized)
    val monthRunRecordList get() = _monthRunRecordList.asStateFlow()

    private val _timeMin = MutableStateFlow("00")
    val timeMin get() = _timeMin.asStateFlow()

    private val _timeHour = MutableStateFlow("0")
    val timeHour get() = _timeHour.asStateFlow()

    private val _distance = MutableStateFlow("0")
    val distance get() = _distance.asStateFlow()

    private val _longestTimeMin = MutableStateFlow("")
    val longestTimeMin get() = _longestTimeMin.asStateFlow()

    private val _longestTimeHour = MutableStateFlow("")
    val longestTimeSec get() = _longestTimeHour.asStateFlow()

    private val _longestDistance = MutableStateFlow("")
    val longestDistance get() = _longestDistance.asStateFlow()

    private val _speed = MutableStateFlow("0")
    val speed get() = _speed.asStateFlow()

    private val _calorie = MutableStateFlow("0")
    val calorie get() = _calorie.asStateFlow()

    private val _errorMsgEvent = SingleLiveEvent<String>()
    val errorMsgEvent get() = _errorMsgEvent


    fun getMyRunRecord(crewSeq: Int){
        viewModelScope.launch(Dispatchers.IO) {
            crewActivityRepository.getMyRunRecord(crewSeq).collectLatest {
                if(it is Result.Success){
                    _monthRunRecordList.value = it
                }
                else if(it is Result.Error) {
                    _errorMsgEvent.postValue("나의 월별 기록을 불러오는 중 오류가 발생했습니다.")
                }
            }
        }
    }

    fun getMyTotalRecord(crewMyTotalRecordDataResponse: CrewMyTotalRecordDataResponse){
        var hour = crewMyTotalRecordDataResponse.totalTime / 3600
        var min = crewMyTotalRecordDataResponse.totalTime / 60
        _timeHour.value = hour.toString()
        _timeMin.value = min.toString()
        if(min < 10){
            _timeMin.value = "0" + _timeMin.value
        }
        val df = DecimalFormat("###0.0")
        _distance.value = df.format((crewMyTotalRecordDataResponse.totalDistance / 1000f))

        _speed.value = String.format("%.1f", crewMyTotalRecordDataResponse.totalAvgSpeed)
        _calorie.value = crewMyTotalRecordDataResponse.totalCalorie.toInt().toString()

    }
}