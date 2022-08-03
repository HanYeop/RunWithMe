package com.ssafy.runwithme.view.my_page.tab.total_record

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.dto.RunRecordDto
import com.ssafy.runwithme.repository.MyActivityRepository
import com.ssafy.runwithme.utils.Result
import com.ssafy.runwithme.utils.SingleLiveEvent
import com.ssafy.runwithme.utils.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
class MyTotalRunRecordViewModel @Inject constructor(
    private val myActivityRepository: MyActivityRepository
) : ViewModel() {

    private val _monthRunRecordList: MutableStateFlow<Result<BaseResponse<List<RunRecordDto>>>>
            = MutableStateFlow(Result.Uninitialized)
    val monthRunRecordList get() = _monthRunRecordList.asStateFlow()

    private val _timeMin = MutableStateFlow("")
    val timeMin get() = _timeMin.asStateFlow()

    private val _timeSec = MutableStateFlow("")
    val timeSec get() = _timeSec.asStateFlow()

    private val _distance = MutableStateFlow("")
    val distance get() = _distance.asStateFlow()

    private val _longestTimeMin = MutableStateFlow("")
    val longestTimeMin get() = _longestTimeMin.asStateFlow()

    private val _longestTimeSec = MutableStateFlow("")
    val longestTimeSec get() = _longestTimeSec.asStateFlow()

    private val _longestDistance = MutableStateFlow("")
    val longestDistance get() = _longestDistance.asStateFlow()

    private val _speed = MutableStateFlow("")
    val speed get() = _speed.asStateFlow()

    private val _calorie = MutableStateFlow("")
    val calorie get() = _calorie.asStateFlow()

    private val _errorMsgEvent = SingleLiveEvent<String>()
    val errorMsgEvent get() = _errorMsgEvent


    fun getMyRunRecord(){
        viewModelScope.launch(Dispatchers.IO) {
            myActivityRepository.getMyRunRecord().collectLatest {
                if(it is Result.Success){
                    _monthRunRecordList.value = it
                }
                else if(it is Result.Error) {
                    _errorMsgEvent.postValue("나의 월별 기록을 불러오는 중 오류가 발생했습니다.")
                }
            }
        }
    }

    fun getMyTotalRecord(){
        viewModelScope.launch(Dispatchers.IO) {
            myActivityRepository.getMyTotalRecord().collectLatest {
                if(it is Result.Success){
                    var min = it.data.data.totalTime / 60
                    var sec = it.data.data.totalTime % 60
                    _timeMin.value = min.toString()
                    _timeSec.value = sec.toString()

                    val df = DecimalFormat("###0.000")
                    _distance.value = df.format((it.data.data.totalDistance / 1000f))

                    min = it.data.data.totalLongestTime / 60
                    sec = it.data.data.totalLongestDistance % 60
                    _longestTimeMin.value = min.toString()
                    _longestTimeSec.value = sec.toString()

                    _longestDistance.value = df.format((it.data.data.totalLongestDistance / 1000f)).toString()

                    _speed.value = String.format("%.1f", it.data.data.totalAvgSpeed)
                    _calorie.value = it.data.data.totalCalorie.toInt().toString()
                } else if(it is Result.Error){
                    _errorMsgEvent.postValue("누적 기록을 불러오는 중 오류가 발생했습니다.")
                }
            }
        }
    }
}