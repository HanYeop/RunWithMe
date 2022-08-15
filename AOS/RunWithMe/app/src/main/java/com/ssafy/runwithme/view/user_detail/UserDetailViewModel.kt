package com.ssafy.runwithme.view.user_detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.dto.EndCrewFileDto
import com.ssafy.runwithme.model.dto.UserDto
import com.ssafy.runwithme.model.response.CrewMyTotalRecordDataResponse
import com.ssafy.runwithme.model.response.OtherUserFileDto
import com.ssafy.runwithme.repository.AchieveRepository
import com.ssafy.runwithme.repository.CrewManagerRepository
import com.ssafy.runwithme.repository.UserRepository
import com.ssafy.runwithme.utils.Result
import com.ssafy.runwithme.utils.SingleLiveEvent
import com.ssafy.runwithme.utils.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {


    private val _userProfile : MutableStateFlow<UserDto> = MutableStateFlow(UserDto(0, 0, "", "", 0, null))
    val userProfile get() = _userProfile.asStateFlow()

    private val _userImage : MutableStateFlow<Int> = MutableStateFlow(0)
    val userImage get() = _userImage.asStateFlow()

    private val _userData : MutableStateFlow<CrewMyTotalRecordDataResponse> = MutableStateFlow(
        CrewMyTotalRecordDataResponse(0, 0, 0.0, 0.0)
    )
    val userData get() = _userData.asStateFlow()

    private val _errorMsgEvent = SingleLiveEvent<String>()
    val errorMsgEvent get() = _errorMsgEvent

    private val _achieveList = MutableStateFlow(arrayListOf<String>())
    val achieveList = _achieveList.asStateFlow()


    fun getUserProfile(userSeq: Int){
        Log.d(TAG, "getUserProfile: uesrSEq; $userSeq")
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.getUserProfile(userSeq).collectLatest {
                if(it is Result.Success){
                    _userProfile.value = it.data.data.userDto
                    _userImage.value = it.data.data.imgFileDto.imgSeq
                    _userData.value = it.data.data.totalRecord

                    Log.d(TAG, "getUserProfile: userProfile : ${userProfile.value}")
                    Log.d(TAG, "getUserProfile: userImage : ${userImage.value}")
                    Log.d(TAG, "getUserProfile: userData : ${userData.value}")
                    
                    val tmpList = arrayListOf<String>()
                    for(i in it.data.data.achieveList){
                        tmpList.add(i.achievementDto.achieveType.lowercase() + i.achievementDto.achieveValue.toInt().toString())
                    }
                    _achieveList.value = tmpList
                }else if(it is Result.Error){
                    _errorMsgEvent.postValue("유저 정보를 불러오는 중 오류가 발생했습니다.")
                }
            }
        }
    }
}