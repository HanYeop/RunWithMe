package com.ssafy.runwithme.view.login

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.runwithme.repository.Oauth2Repository
import com.ssafy.runwithme.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val oauth2Repository: Oauth2Repository,
    private val sharedPreferences: SharedPreferences,
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

    private val _fcmEvent = SingleLiveEvent<String>()
    val fcmEvent :LiveData<String> get() = _fcmEvent

    fun googleLogin(code: String){
        viewModelScope.launch(Dispatchers.IO){
            oauth2Repository.googleLogin(code).collectLatest {
                if(it is Result.Success) {
                    // 등록 되지 않은 사용자 회원 가입
                    if (!it.data.isRegistered) {
                        _joinMsgEvent.postValue("폰에서 회원가입하고 오세요.")
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

    fun kakaoLogin(code: String){
        viewModelScope.launch(Dispatchers.IO){
            oauth2Repository.kakaoLogin(code).collectLatest {
                if(it is Result.Success) {
                    // 등록 되지 않은 사용자 회원 가입
                    if (!it.data.isRegistered) {
                        _joinMsgEvent.postValue("폰에서 회원가입하고 오세요.")
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

    fun naverLogin(code: String, email: String){
        viewModelScope.launch(Dispatchers.IO){
            oauth2Repository.naverLogin(code, email).collectLatest {
                if(it is Result.Success) {
                    // 등록 되지 않은 사용자 회원 가입
                    if (!it.data.isRegistered) {
                        _joinMsgEvent.postValue("폰에서 회원가입하고 오세요.")
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



}
