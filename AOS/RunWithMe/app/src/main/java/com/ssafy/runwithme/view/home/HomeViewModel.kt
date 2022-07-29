package com.ssafy.runwithme.view.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.response.MyCurrentCrewResponse
import com.ssafy.runwithme.model.response.RankingResponse
import com.ssafy.runwithme.repository.CrewManagerRepository
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
    private val totalRankingRepository: TotalRankingRepository
) : ViewModel(){

    private val _myCurrentCrewList: MutableStateFlow<Result<BaseResponse<List<MyCurrentCrewResponse>>>>
        = MutableStateFlow(Result.Uninitialized)
    val myCurrentCrewList get() = _myCurrentCrewList.asStateFlow()

    private val _totalRanking: MutableStateFlow<Result<BaseResponse<List<RankingResponse>>>>
        = MutableStateFlow(Result.Uninitialized)
    val totalRanking get() = _totalRanking.asStateFlow()

    private val _myRanking: MutableStateFlow<RankingResponse>
        = MutableStateFlow(RankingResponse("", 0, 0, -1, 0))
    val myRanking get() = _myRanking.asStateFlow()

    private val _firstRanking: MutableStateFlow<RankingResponse>
            = MutableStateFlow(RankingResponse("", 0, 0, -1, 0))
    val firstRanking get() = _firstRanking.asStateFlow()

    private val _secRanking: MutableStateFlow<RankingResponse>
            = MutableStateFlow(RankingResponse("", 0, 0, -1, 0))
    val secRanking get() = _secRanking.asStateFlow()

    private val _thirdRanking: MutableStateFlow<RankingResponse>
            = MutableStateFlow(RankingResponse("", 0, 0, -1, 0))
    val thirdRanking get() = _thirdRanking.asStateFlow()

    private val _unit = MutableStateFlow("km")
    val unit get() = _unit.asStateFlow()

    private val _errorMsgEvent = SingleLiveEvent<String>()
    val errorMsgEvent get() = _errorMsgEvent

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
                    if(type.equals("distance")){
                        var len = it.data.data.size
                        for(i : Int in 0 until len){
                            when(i){
                                0 -> _firstRanking.value = it.data.data[i]
                                1 -> _secRanking.value = it.data.data[i]
                                2 -> _thirdRanking.value = it.data.data[i]
                                else -> break
                            }
                        }
                    }
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

    private fun syncUnit(type : String){
        _unit.value = when(type){
            "distance" -> "km"
            "time" -> "분"
            "point" -> "P"
            else -> ""
        }
    }
}
