package com.ssafy.runwithme.view.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.runwithme.repository.Oauth2Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val oauth2Repository: Oauth2Repository
): ViewModel(){

    fun googleLogin(){
        viewModelScope.launch(Dispatchers.Main){
            oauth2Repository.googleLogin().collectLatest {
                Log.d("test5", "googleLogin: $it")
            }
        }
    }
}