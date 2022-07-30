package com.ssafy.runwithme.view.login

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.runwithme.model.dto.UserDto
import com.ssafy.runwithme.repository.Oauth2Repository
import com.ssafy.runwithme.repository.UserRepository
import com.ssafy.runwithme.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val oauth2Repository: Oauth2Repository,
    private val sharedPreferences: SharedPreferences,
    private val userRepository: UserRepository
): ViewModel(){

    private val _email = MutableStateFlow("")
    val email get() = _email.asStateFlow()

    private val _joinEvent = SingleLiveEvent<String>()
    val joinEvent :LiveData<String> get() = _joinEvent

    private val _loginEvent = SingleLiveEvent<String>()
    val loginEvent :LiveData<String> get() = _loginEvent

    private val _errorMsgEvent = SingleLiveEvent<String>()
    val errorMsgEvent get() = _errorMsgEvent

    private val _failMsgEvent = SingleLiveEvent<String>()
    val failMsgEvent get() = _failMsgEvent

    private val _joinMsgEvent = SingleLiveEvent<String>()
    val joinMsgEvent get() = _joinMsgEvent

    // TODO : 메세지 임의 변경
    fun googleLogin(code: String){
        viewModelScope.launch(Dispatchers.IO){
            oauth2Repository.googleLogin(code).collectLatest {
                if(it is Result.Success) {
                    // 등록 되지 않은 사용자 회원 가입
                    if (!it.data.isRegistered) {
                        _email.value = it.data.email
                        _joinEvent.postValue(it.data.jwtToken)
                        _joinMsgEvent.postValue("회원 가입 페이지로 이동합니다.")
                    } else {
                        sharedPreferences.edit().putString(USER, it.data.userSeq.toString()).apply()
                        // 이미 등록된 사용자라서 토큰 바로 저장
                        sharedPreferences.edit().putString(JWT, it.data.jwtToken).apply()
                        _loginEvent.postValue("로그인 완료")
                    }
                }else if (it is Result.Error){
                    _errorMsgEvent.postValue("서버 에러 발생")
                }
            }
        }
    }

    fun joinUser(token: String, userDto: UserDto) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.joinUser(token, userDto).collectLatest {
                if(it is Result.Success) {
                    Log.d(TAG, "joinUser: ${it.data.data.userSeq}")
                    sharedPreferences.edit().putString(USER, it.data.data.userSeq.toString()).apply()
                    sharedPreferences.edit().putString(JWT,it.data.data.jwtToken).apply()
                    _loginEvent.postValue("로그인 완료")
                }else if(it is Result.Fail){

                } else if (it is Result.Error){
                    _errorMsgEvent.postValue("서버 에러 발생")
                }
            }
        }
    }

}
