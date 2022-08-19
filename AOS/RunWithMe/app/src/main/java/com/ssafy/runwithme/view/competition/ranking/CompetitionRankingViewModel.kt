package com.ssafy.runwithme.view.competition.ranking

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.response.RankingResponse
import com.ssafy.runwithme.repository.CompetitionRepository
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
class CompetitionRankingViewModel @Inject constructor(
    private val competitionRepository: CompetitionRepository
): ViewModel() {

    private val _totalRankingList: MutableStateFlow<Result<BaseResponse<List<RankingResponse>>>> = MutableStateFlow(
        Result.Uninitialized)
    val totalRankingList get() = _totalRankingList.asStateFlow()

    private val _myRanking: MutableStateFlow<RankingResponse> = MutableStateFlow(RankingResponse("", 0, 0, 0, 0, "NOTRANKED"))
    val myRanking get() = _myRanking.asStateFlow()

    private val _errorMsgEvent = SingleLiveEvent<String>()
    val errorMsgEvent get() = _errorMsgEvent

    private val _successMsgEvent = SingleLiveEvent<String>()
    val successMsgEvent get() = _successMsgEvent

    fun getCompetitionTotalRanking(competitionSeq: Int){
        viewModelScope.launch(Dispatchers.IO) {
            competitionRepository.getCompetitionTotalRanking(competitionSeq).collectLatest {
                if(it is Result.Success){
                    _totalRankingList.value = it
                    Log.d("test5", "getCompetitionTotalRanking: ${it.data.data}")
                }else if(it is Result.Error){
                    _errorMsgEvent.postValue("잠시 후 시도해주세요.")
                }
            }
        }
    }

    fun getCompetitionMyRanking(competitionSeq: Int, userSeq: Int){
        viewModelScope.launch(Dispatchers.IO) {
            competitionRepository.getCompetitionMyRanking(competitionSeq, userSeq).collectLatest {
                if(it is Result.Success){
                    _myRanking.value = it.data.data
                    Log.d("test5", "getCompetitionMyRanking: ${it.data.data}")
                }else if(it is Result.Error){
                    _errorMsgEvent.postValue("잠시 후 시도해주세요.")
                }
            }
        }
    }

}