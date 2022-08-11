package com.ssafy.runwithme.view.crew_detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.dto.PasswordDto
import com.ssafy.runwithme.model.response.CrewMyTotalRecordDataResponse
import com.ssafy.runwithme.model.response.MyGraphDataResponse
import com.ssafy.runwithme.repository.CrewActivityRepository
import com.ssafy.runwithme.repository.CrewManagerRepository
import com.ssafy.runwithme.repository.CrewRepository
import com.ssafy.runwithme.utils.Result
import com.ssafy.runwithme.utils.SingleLiveEvent
import com.ssafy.runwithme.utils.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CrewDetailViewModel @Inject constructor(
    private val crewActivityRepository: CrewActivityRepository,
    private val crewManagerRepository: CrewManagerRepository,
    private val crewRepository: CrewRepository
) : ViewModel() {

    private val _isCrewMember: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isCrewMember get() = _isCrewMember.asStateFlow()

    private val _crewState: MutableStateFlow<String> = MutableStateFlow("await")
    val crewState get() = _crewState.asStateFlow()
    // await : 시작 전, start : 시작, end : 끝난 후, 러닝 시간 아닌 경우

    private val _myGraphData: MutableStateFlow<Result<BaseResponse<List<MyGraphDataResponse>>>> =
        MutableStateFlow(Result.Uninitialized)
    val myGraphData get() = _myGraphData.asStateFlow()

    private val _myTotalRecordData: MutableStateFlow<CrewMyTotalRecordDataResponse> =
        MutableStateFlow(
            CrewMyTotalRecordDataResponse(0, 0, 0.0, 0.0)
        )
    val myTotalRecordData get() = _myTotalRecordData.asStateFlow()

    private val _successMsgEvent = SingleLiveEvent<String>()
    val successMsgEvent get() = _successMsgEvent

    private val _errorMsgEvent = SingleLiveEvent<String>()
    val errorMsgEvent get() = _errorMsgEvent

    private val _isCrewManager: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isCrewManager get() = _isCrewManager.asStateFlow()


    fun setState(start: String, end: String, timeStart: String, timeEnd: String) {
        val today = Calendar.getInstance()
        val sf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        var startDate = sf.parse(start)
        var endDate = sf.parse(end)


        val sfTime = SimpleDateFormat("HH:mm:ss", Locale.KOREA)

        var startTime = sfTime.parse(timeStart)
        var endTime = sfTime.parse(timeEnd)

        val now = System.currentTimeMillis()
        val date = Date(now)
        val nowString = sfTime.format(date)
        val nowTime = sfTime.parse(nowString)

        val startCalendar = Calendar.getInstance()
        startCalendar.time = startTime

        val endCalendar = Calendar.getInstance()
        endCalendar.time = endTime

        if (today.time.time - startDate.time > 0) {
            if (today.time.time - endDate.time > 0) {
                _crewState.value = "end"
            } else {

                if (nowTime.time >= startTime.time && nowTime.time<= endTime.time) {
                    _crewState.value = "start"
                } else {
                    _crewState.value = "end"
                }

//                if (date.time >= startTime.time && date.time <= endTime.time) {
//                    _crewState.value = "start"
//                } else {
//                    _crewState.value = "end"
//                }
            }

        } else {
            _crewState.value = "await"
        }


    }


    fun checkCrewMember(crewSeq: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            crewManagerRepository.checkCrewMember(crewSeq).collectLatest {
                if (it is Result.Success) {
                    _isCrewMember.value = it.data.data
                }
            }
        }
    }

    fun checkCrewManager(isCrewManager : Boolean){
        Log.d(TAG, "checkCrewManager: $isCrewManager")
        _isCrewManager.value = isCrewManager
    }

    fun joinCrew(crewId: Int, password: String?) {
        var passwordDto: PasswordDto? = null
        if (password != null) {
            passwordDto = PasswordDto(password)
        }
        Log.d(TAG, "joinCrew: password : $passwordDto")
        viewModelScope.launch(Dispatchers.IO) {
            crewRepository.joinCrew(crewId, passwordDto).collectLatest {
                Log.d(TAG, "joinCrew: $it")
                if (it is Result.Success) {
                    _successMsgEvent.postValue("가입 완료했습니다.")
                } else if (it is Result.Fail) {
                    _errorMsgEvent.postValue(it.data.msg)
                } else if (it is Result.Error) {
                    Log.d(TAG, "joinCrew: ")
                    _errorMsgEvent.postValue("서버 에러 입니다.")
                }
            }
        }
    }

    fun getMyGraphData(crewSeq: Int, goalType: String) {
        viewModelScope.launch(Dispatchers.IO) {
            crewActivityRepository.getMyGraphData(crewSeq, goalType).collectLatest {
                if (it is Result.Success) {
                    _myGraphData.value = it
                } else if (it is Result.Fail) {
                    _errorMsgEvent.postValue(it.data.msg)
                } else if (it is Result.Error) {
                    Log.d(TAG, "getMyGraphData: ")
                    _errorMsgEvent.postValue("서버 에러 입니다.")
                }
            }
        }
    }

    fun getTotalRecordData(crewSeq: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            crewActivityRepository.getMyTotalRecordData(crewSeq).collectLatest {
                if (it is Result.Success) {
                    _myTotalRecordData.value = it.data.data
                } else if(it is Result.Error) {
                    _errorMsgEvent.postValue("서버 에러 입니다.")
                }
            }
        }
    }

    fun resignCrew(crewSeq: Int){
        viewModelScope.launch (Dispatchers.IO){
            crewManagerRepository.resignCrew(crewSeq).collectLatest {
                if (it is Result.Success){
                    _successMsgEvent.postValue("탈퇴되었습니다.")
                }else if(it is Result.Error){
                    _errorMsgEvent.postValue("서버 에러 입니다.")
                }else if(it is Result.Fail){
                    _errorMsgEvent.postValue("탈퇴 되지 않았습니다.")
                }
            }
        }
    }

    fun deleteCrew(crewSeq: Int){
        viewModelScope.launch (Dispatchers.IO){
            crewManagerRepository.deleteCrew(crewSeq).collectLatest {
                if (it is Result.Success){
                    _successMsgEvent.postValue("해체되었습니다.")
                }else if(it is Result.Error){
                    _errorMsgEvent.postValue("서버 에러 입니다.")
                }else if(it is Result.Fail){
                    _errorMsgEvent.postValue("해체 되지 않았습니다.")
                }
            }
        }
    }

}