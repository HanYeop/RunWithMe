package com.ssafy.runwithme.view.my_page

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.ssafy.runwithme.model.dto.ProfileEditDto
import com.ssafy.runwithme.repository.MyActivityRepository
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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject


@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val myActivityRepository: MyActivityRepository,
    private val userRepository: UserRepository
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

    private val _competitionResult : MutableStateFlow<String?> = MutableStateFlow(null)
    val competitionResult get() = _competitionResult.asStateFlow()

    private val _errorMsgEvent = SingleLiveEvent<String>()
    val errorMsgEvent get() = _errorMsgEvent

    private val _editMsgEvent = SingleLiveEvent<String>()
    val editMsgEvent get() = _editMsgEvent

    private val _logoutEvent = SingleLiveEvent<String>()
    val logoutEvent get() = _logoutEvent

    fun getMyProfile(){
        viewModelScope.launch(Dispatchers.IO) {
            myActivityRepository.getMyProfile().collectLatest {
                if(it is Result.Success){
                    Log.d(TAG, "getMyProfile: $it")
                    _imgSeq.value = it.data.data.imageFileDto.imgSeq
                    _email.value = it.data.data.userDto.email
                    _nickName.value = it.data.data.userDto.nickName
                    _height.value = it.data.data.userDto.height.toString()
                    _weight.value = it.data.data.userDto.weight.toString()
                    _point.value = it.data.data.userDto.point.toString()
                    _competitionResult.value = it.data.data.userDto.competitionResult
                } else if(it is Result.Error){
                    _errorMsgEvent.postValue("프로필 정보를 불러오는 중 오류가 발생했습니다.")
                }
            }
        }
    }

    fun editMyProfile(profile: ProfileEditDto, imgFile: MultipartBody.Part?){
        val json = Gson().toJson(profile)
        val profileEditDto = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), json)

        Log.d("test5", "editMyProfile: $json $imgFile ")

        viewModelScope.launch(Dispatchers.IO) {
            myActivityRepository.editMyProfile(profileEditDto, imgFile).collectLatest {
                if(it is Result.Success){
                    _editMsgEvent.postValue("프로필 수정에 성공했습니다.")
                } else if(it is Result.Fail){
                    if(it.data.msg == "처리되지 않은 에러"){
                        _errorMsgEvent.postValue("이미 사용중인 닉네임입니다.")
                    }
                    else{
                        _errorMsgEvent.postValue(it.data.msg)
                    }
                } else if(it is Result.Error) {
                    _errorMsgEvent.postValue("프로필 수정에 실패했습니다.")
                }
            }
        }
    }

    fun deleteFcmToken(){
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.deleteFcmToken().collectLatest {
                Log.d(TAG, "deleteFcmToken: $it")
                if(it is Result.Success){
                    _logoutEvent.postValue("로그아웃 완료")
                }
            }
        }
    }

}