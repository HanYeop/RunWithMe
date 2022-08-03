package com.ssafy.runwithme.view.crew_detail.ranking

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.response.RankingResponse
import com.ssafy.runwithme.repository.CrewActivityRepository
import com.ssafy.runwithme.repository.MyActivityRepository
import com.ssafy.runwithme.repository.TotalRankingRepository
import com.ssafy.runwithme.utils.Result
import com.ssafy.runwithme.utils.SingleLiveEvent
import com.ssafy.runwithme.utils.TAG
import com.ssafy.runwithme.utils.USER
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CrewUserRankingViewModel @Inject constructor(
    private val crewActivityRepository: CrewActivityRepository,
    private val sharedPreferences: SharedPreferences,
    private val myActivityRepository: MyActivityRepository
) : ViewModel() {

    private val _crewRanking: MutableStateFlow<Result<BaseResponse<List<RankingResponse>>>>
            = MutableStateFlow(Result.Uninitialized)
    val crewRanking get() = _crewRanking.asStateFlow()

    private val _myRanking: MutableStateFlow<RankingResponse>
            = MutableStateFlow(RankingResponse("", 0, 0, -1, 0))
    val myRanking get() = _myRanking.asStateFlow()

    private val _goalType: MutableStateFlow<String> = MutableStateFlow("km")
    val goalType get() = _goalType.asStateFlow()

    private val _errorMsgEvent = SingleLiveEvent<String>()
    val errorMsgEvent get() = _errorMsgEvent

    private val _imgSeq = MutableStateFlow(0)
    val imgSeq get() = _imgSeq.asStateFlow()

    private val _nickName = MutableStateFlow("")
    val nickname get() = _nickName.asStateFlow()

    private val _point = MutableStateFlow("")
    val point get() = _point.asStateFlow()

    private val userSeq: Int = sharedPreferences.getString(USER, "0")!!.toInt()


    fun getCrewRanking(crewSeq: Int, type : String) {
        syncUnit(type)

        viewModelScope.launch(Dispatchers.IO) {
            crewActivityRepository.getCrewRanking(crewSeq, type).collectLatest {
                if(it is Result.Success){
                    _crewRanking.value = it
                    getMyRanking(it.data.data)
                    Log.d(TAG, "getCrewRanking: $it")
                } else if(it is Result.Error){
                    _errorMsgEvent.postValue("크루원 랭킹 불러오기 중 오류가 발생했습니다.")
                }
            }
        }
    }

    fun getMyRanking(rankingResponseList : List<RankingResponse>) {
        for (rankingResponse in rankingResponseList) {
            if(rankingResponse.userSeq == userSeq){
                _myRanking.value = rankingResponse
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
        Log.d(TAG, "syncUnit: $type")
        _goalType.value = when(type){
            "distance" -> "km"
            "time" -> "분"
            "point" -> "P"
            else -> ""
        }
    }

}