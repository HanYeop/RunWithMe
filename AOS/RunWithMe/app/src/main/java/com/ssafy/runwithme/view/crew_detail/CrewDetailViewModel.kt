package com.ssafy.runwithme.view.crew_detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.runwithme.model.dto.*
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
) : ViewModel(){

    private val _isCrewMember: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isCrewMember get() = _isCrewMember.asStateFlow()

    private val _crewState: MutableStateFlow<String> = MutableStateFlow("await")
    val crewState get() = _crewState.asStateFlow()

    // await : 시작 전, start : 시작, end : 끝난 후
    private val _checkCrewMemberDto: MutableStateFlow<CheckCrewMemberDto> = MutableStateFlow(
        CheckCrewMemberDto(false, "")
    )
    val checkCrewMemberDto get() = _checkCrewMemberDto.asStateFlow()

//    private val _crewState: MutableStateFlow<>

    private val _successMsgEvent = SingleLiveEvent<String>()
    val successMsgEvent get() = _successMsgEvent

    private val _errorMsgEvent = SingleLiveEvent<String>()
    val errorMsgEvent get() = _errorMsgEvent


    fun setState(start: String, end: String) {
        val today = Calendar.getInstance()
        val sf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")

        var startDate = sf.parse(start)
        var endDate = sf.parse(end)

        if(today.time.time - startDate.time > 0){
            if(today.time.time - endDate.time > 0){
                _checkCrewMemberDto.value.crewState = "end"
                _crewState.value = "end"
            }else{
                _checkCrewMemberDto.value.crewState = "start"
                _crewState.value = "start"
            }

        }else{
            _checkCrewMemberDto.value.crewState = "await"
            _crewState.value = "await"
        }

        Log.d(TAG, "getState: startDate : $startDate, endDate : $endDate, state : ${_checkCrewMemberDto.value}")

    }


    fun checkCrewMember(crewSeq: Int){
        viewModelScope.launch (Dispatchers.IO) {
            crewManagerRepository.checkCrewMember(crewSeq).collectLatest {
                if(it is Result.Success){
                    _checkCrewMemberDto.value.isCrewMember = it.data.data
                    _isCrewMember.value = it.data.data
                    Log.d(TAG, "checkCrewMember: crewSeq : $crewSeq, isCrewMember : ${_checkCrewMemberDto.value.isCrewMember}")
                }
            }
        }
    }

    fun joinCrew(crewId: Int, password: String?){
        var passwordDto : PasswordDto? = null
        if(password != null){
            passwordDto = PasswordDto(password)
        }
        Log.d(TAG, "joinCrew: password : $passwordDto")
        viewModelScope.launch (Dispatchers.IO) {
            crewRepository.joinCrew(crewId, passwordDto).collectLatest {
                Log.d(TAG, "joinCrew: $it")
                if(it is Result.Success){
                    _successMsgEvent.postValue("가입 완료했습니다.")
                }else if(it is Result.Fail){
                    _errorMsgEvent.postValue(it.data.msg)
                }else if(it is Result.Error){
                    _errorMsgEvent.postValue("서버 에러 입니다.")
                }
            }
        }
    }

}