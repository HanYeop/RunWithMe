package com.ssafy.runwithme.view.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.response.MyCurrentCrewResponse
import com.ssafy.runwithme.model.response.RankingResponse
import com.ssafy.runwithme.repository.CrewManagerRepository
import com.ssafy.runwithme.repository.MyActivityRepository
import com.ssafy.runwithme.repository.TotalRankingRepository
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
class HomeViewModel @Inject constructor(
    private val crewManagerRepository: CrewManagerRepository,
    private val totalRankingRepository: TotalRankingRepository,
    private val myActivityRepository: MyActivityRepository
) : ViewModel(){

    private val _myCurrentCrewList: MutableStateFlow<Result<BaseResponse<List<MyCurrentCrewResponse>>>>
        = MutableStateFlow(Result.Uninitialized)
    val myCurrentCrewList get() = _myCurrentCrewList.asStateFlow()

    private val _totalRanking: MutableStateFlow<Result<BaseResponse<List<RankingResponse>>>>
        = MutableStateFlow(Result.Uninitialized)
    val totalRanking get() = _totalRanking.asStateFlow()

    private val _myRanking: MutableStateFlow<RankingResponse>
        = MutableStateFlow(RankingResponse("", 0, 0, -1, 0, null))
    val myRanking get() = _myRanking.asStateFlow()

    private val _unit = MutableStateFlow("km")
    val unit get() = _unit.asStateFlow()

    private val _errorMsgEvent = SingleLiveEvent<String>()
    val errorMsgEvent get() = _errorMsgEvent

    private val _imgSeq = MutableStateFlow(0)
    val imgSeq get() = _imgSeq.asStateFlow()

    private val _nickName = MutableStateFlow("")
    val nickname get() = _nickName.asStateFlow()

    private val _point = MutableStateFlow("")
    val point get() = _point.asStateFlow()

    fun getMyCurrentCrew(){
        viewModelScope.launch(Dispatchers.IO) {
            crewManagerRepository.getMyCurrentCrew().collectLatest{
                if(it is Result.Success){
                    _myCurrentCrewList.value = it
                }else if(it is Result.Error){
                    _errorMsgEvent.postValue("내 크루 불러오기 중 오류가 발생했습니다.")
                }
            }
        }
    }

    fun getTotalRanking(type : String, size : Int, offset : Int) {
        syncUnit(type)

        viewModelScope.launch(Dispatchers.IO) {
            totalRankingRepository.getTotalRanking(type, size, offset).collectLatest {
                Log.d(TAG, "getTotalRanking: $it")
                if(it is Result.Success){
                    _totalRanking.value = it
                } else if(it is Result.Error){
                    _errorMsgEvent.postValue("전체 랭킹 불러오기 중 오류가 발생했습니다.")
                }
            }
        }
    }

    fun getMyRanking(type : String) {
        syncUnit(type)

        viewModelScope.launch(Dispatchers.IO) {
            totalRankingRepository.getMyRanking(type).collectLatest {
                if(it is Result.Success){
                    _myRanking.value = it.data.data
                } else if(it is Result.Error){
                    _errorMsgEvent.postValue("내 랭킹 불러오기 중 오류가 발생했습니다.")
                }
            }
        }
    }

    fun getMyProfile(){
        viewModelScope.launch(Dispatchers.IO) {
            myActivityRepository.getMyProfile().collectLatest {
                if(it is Result.Success){
                    _imgSeq.value = it.data.data.imageFileDto.imgSeq
                    _nickName.value = it.data.data.userDto.nickName
                    _point.value = it.data.data.userDto.point.toString()
                } else if(it is Result.Error){
                    _errorMsgEvent.postValue("프로필 정보를 불러오는 중 오류가 발생했습니다.")
                }
            }
        }
    }

    private fun syncUnit(type : String){
        _unit.value = when(type){
            "distance" -> "km"
            "time" -> "분"
            "point" -> "P"
            else -> ""
        }
    }
}
