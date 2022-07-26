package com.ssafy.runwithme.view.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.response.MyCurrentCrewResponse
import com.ssafy.runwithme.repository.CrewManagerRepository
import com.ssafy.runwithme.utils.Result
import com.ssafy.runwithme.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val crewManagerRepository: CrewManagerRepository
) : ViewModel(){

    private val _myCurrentCrewList: MutableStateFlow<Result<BaseResponse<List<MyCurrentCrewResponse>>>>
        = MutableStateFlow(Result.Uninitialized)
    val myCurrentCrewList get() = _myCurrentCrewList.asStateFlow()

    private val _errorMsgEvent = SingleLiveEvent<String>()
    val errorMsgEvent get() = _errorMsgEvent

    fun getMyCurrentCrew(){
        viewModelScope.launch(Dispatchers.IO) {
            crewManagerRepository.getMyCurrentCrew().collectLatest{
                if(it is Result.Success){
                    _myCurrentCrewList.value = it
                }else if(it is Result.Error){
                    _errorMsgEvent.postValue("오류가 발생했습니다.")
                }
            }
        }
    }
}
