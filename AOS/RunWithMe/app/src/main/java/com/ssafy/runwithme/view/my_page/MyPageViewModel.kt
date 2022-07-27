package com.ssafy.runwithme.view.my_page

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun getMyProfile(){
        viewModelScope.launch(Dispatchers.IO) {
            myActivityRepository.getMyProfile().collectLatest {
                if(it is Result.Success){
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
}