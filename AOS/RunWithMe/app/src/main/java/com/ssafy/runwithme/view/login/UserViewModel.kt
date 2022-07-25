package com.ssafy.runwithme.view.login

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.runwithme.model.dto.UserDto
import com.ssafy.runwithme.repository.Oauth2Repository
import com.ssafy.runwithme.repository.UserRepository
import com.ssafy.runwithme.utils.JWT
import com.ssafy.runwithme.utils.Result
import com.ssafy.runwithme.utils.SingleLiveEvent
import com.ssafy.runwithme.utils.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val oauth2Repository: Oauth2Repository,
    private val sharedPreferences: SharedPreferences,
    private val userRepository: UserRepository
): ViewModel(){

    private val _joinEvent = SingleLiveEvent<String>()
    val joinEvent :LiveData<String> get() = _joinEvent

    private val _loginEvent = SingleLiveEvent<String>()
    val loginEvent :LiveData<String> get() = _loginEvent

    fun googleLogin(code: String){
        viewModelScope.launch(Dispatchers.IO){
            oauth2Repository.googleLogin(code).collectLatest {
                if(!it.isRegistered){
                    _joinEvent.postValue(it.jwtToken)
                }
                // 이미 등록된 사용자라서 토큰 바로 저장
                else{
                    sharedPreferences.edit().putString(JWT,it.jwtToken).apply()
                    _loginEvent.postValue(it.msg)
                }
            }
        }
    }

    fun joinUser(token: String, userDto: UserDto) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.joinUser(token, userDto).collectLatest {
                if(it is Result.Success) {
                    sharedPreferences.edit().putString(JWT,it.data.data.jwtToken).apply()
                    _loginEvent.postValue(it.data.msg)
                }
            }
        }
    }
}