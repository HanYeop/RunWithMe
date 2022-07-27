package com.ssafy.runwithme.view.my_page

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.runwithme.model.dto.UserDto
import com.ssafy.runwithme.repository.MyActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import com.ssafy.runwithme.utils.Result
import com.ssafy.runwithme.utils.SingleLiveEvent
import com.ssafy.runwithme.utils.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val myActivityRepository: MyActivityRepository
) : ViewModel() {

    private val _imgSeq = MutableStateFlow(0)
    val imgSeq get() = _imgSeq.asStateFlow()

    private val _email = MutableStateFlow("")
    val email get() = _email.asStateFlow()

    private val _nickName = MutableStateFlow("")
    val nickname get() = _nickName.asStateFlow()

    private val _height = MutableStateFlow("")
    val height get() = _height.asStateFlow()

    private val _weight = MutableStateFlow("")
    val weight get() = _weight.asStateFlow()

    private val _point = MutableStateFlow("")
    val point get() = _point.asStateFlow()

    private val _errorMsgEvent = SingleLiveEvent<String>()
    val errorMsgEvent get() = _errorMsgEvent

    private val _editMsgEvent = SingleLiveEvent<String>()
    val editMsgEvent get() = _editMsgEvent

    fun getMyProfile(){
        viewModelScope.launch(Dispatchers.IO) {
            myActivityRepository.getMyProfile().collectLatest {
                if(it is Result.Success){
                    // _imgSeq.value = it.data.data.imageFileDto.imgSeq
                    _email.value = it.data.data.userDto.email
                    _nickName.value = it.data.data.userDto.nickName
                    _height.value = it.data.data.userDto.height.toString()
                    _weight.value = it.data.data.userDto.weight.toString()
                    _point.value = it.data.data.userDto.point.toString()
                } else {
                    _errorMsgEvent.postValue("프로필 정보를 불러오는 중 오류가 발생했습니다.")
                }
            }
        }
    }

    fun editMyProfile(userDto: UserDto){
        viewModelScope.launch(Dispatchers.IO) {
            myActivityRepository.editMyProfile(userDto).collectLatest {
                if(it is Result.Success){
                    Log.d(TAG, "editMyProfile: $it")
                    _editMsgEvent.postValue("프로필 수정에 성공했습니다.")
                } else {
                    _errorMsgEvent.postValue("프로필 수정에 실패했습니다.")
                }
            }
        }
    }

}