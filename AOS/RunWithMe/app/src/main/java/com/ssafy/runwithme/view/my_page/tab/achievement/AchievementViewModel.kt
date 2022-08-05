package com.ssafy.runwithme.view.my_page.tab.achievement

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.dto.CrewDto
import com.ssafy.runwithme.model.dto.EndCrewFileDto
import com.ssafy.runwithme.repository.AchieveRepository
import com.ssafy.runwithme.repository.CrewManagerRepository
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
class AchievementViewModel @Inject constructor(
    private val achieveRepository: AchieveRepository,
    private val crewManagerRepository: CrewManagerRepository
) : ViewModel() {

    private val _endCrewFileDto : MutableStateFlow<Result<BaseResponse<List<EndCrewFileDto>>>>
        = MutableStateFlow(Result.Uninitialized)
    val endCrewFileDto get() = _endCrewFileDto

    private val _errorMsgEvent = SingleLiveEvent<String>()
    val errorMsgEvent get() = _errorMsgEvent

    private val _achieveList = MutableStateFlow(arrayListOf<String>())
    val achieveList = _achieveList.asStateFlow()

    fun getMyAchieve(){
        viewModelScope.launch(Dispatchers.IO) {
            achieveRepository.getMyAchieve().collectLatest {
                if(it is Result.Success){
                    val tmpList = arrayListOf<String>()
                    for(i in it.data.data){
                        Log.d(TAG, "getMyAchieve: ${i.achievementDto.achieveType.lowercase() + i.achievementDto.achieveValue.toInt().toString()}")
                        tmpList.add(i.achievementDto.achieveType.lowercase() + i.achievementDto.achieveValue.toInt().toString())
                    }
                    _achieveList.value = tmpList
                }
                else if(it is Result.Error){
                    _errorMsgEvent.postValue("업적 목록을 불러오는 중 오류가 발생했습니다.")
                }
            }
        }
    }

    fun getMyEndCrew(){
        viewModelScope.launch {
            crewManagerRepository.getMyEndCrew().collectLatest {
                if(it is Result.Success){
                    _endCrewFileDto.value = it
                    Log.d(TAG, "getMyEndCrew: $it")
                } else if(it is Result.Error){
                    _errorMsgEvent.postValue("종료된 크루목록을 불러오는 중 오류가 발생했습니다.")
                }
            }
        }
    }

}