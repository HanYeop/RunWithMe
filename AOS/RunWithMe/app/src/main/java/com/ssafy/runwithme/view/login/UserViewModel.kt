package com.ssafy.runwithme.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.runwithme.repository.Oauth2Repository
import com.ssafy.runwithme.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val oauth2Repository: Oauth2Repository
): ViewModel(){

    private val _joinEvent = SingleLiveEvent<String>()
    val joinEvent :LiveData<String> get() = _joinEvent

    private val _loginEvent = SingleLiveEvent<String>()
    val loginEvent :LiveData<String> get() = _loginEvent

    fun googleLogin(code: String){
        viewModelScope.launch(Dispatchers.IO){
            oauth2Repository.googleLogin(code).collectLatest {
                if(!it.isRegistered){
                    _joinEvent.postValue(it.msg)
                }else{
                    _loginEvent.postValue(it.msg)
                }
            }
        }
    }
}