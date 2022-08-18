package com.ssafy.runwithme.view.competition

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.runwithme.model.dto.CompetitionDto
import com.ssafy.runwithme.model.response.RankingResponse
import com.ssafy.runwithme.repository.CompetitionRepository
import com.ssafy.runwithme.repository.CrewActivityRepository
import com.ssafy.runwithme.repository.CrewManagerRepository
import com.ssafy.runwithme.repository.CrewRepository
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
class CompetitionViewModel @Inject constructor(
    private val competitionRepository: CompetitionRepository
) : ViewModel() {

    private val _competitionDto: MutableStateFlow<CompetitionDto> = MutableStateFlow(CompetitionDto("", "", 0))
    val competitionDto get() = _competitionDto.asStateFlow()

    private val _competitionImageSeq: MutableStateFlow<Int> = MutableStateFlow(0)
    val competitionImageSeq get() = _competitionImageSeq.asStateFlow()

    private val _rankingFirst: MutableStateFlow<RankingResponse> = MutableStateFlow(RankingResponse("", 0, 0, 0, 0, ""))
    val rankingFirst get() = _rankingFirst.asStateFlow()

    private val _rankingSecond: MutableStateFlow<RankingResponse> = MutableStateFlow(RankingResponse("", 0, 0, 0, 0, ""))
    val rankingSecond get() = _rankingSecond.asStateFlow()

    private val _rankingThird: MutableStateFlow<RankingResponse> = MutableStateFlow(RankingResponse("", 0, 0, 0, 0, ""))
    val rankingThird get() = _rankingThird.asStateFlow()

    private val _participants: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val participants get() = _participants.asStateFlow()

    private val _successMsgEvent = SingleLiveEvent<String>()
    val successMsgEvent get() = _successMsgEvent

    private val _errorMsgEvent = SingleLiveEvent<String>()
    val errorMsgEvent get() = _errorMsgEvent

    var userSeq: Int = 0

    fun getInprogressCompetition() {
        viewModelScope.launch(Dispatchers.IO) {
            competitionRepository.getInprogressCompetition().collectLatest {
                if (it is Result.Success){
                    _competitionDto.value = it.data.data.competitionDto
                    Log.d(TAG, "getInprogressCompetition: ${competitionDto.value}")
                    _competitionImageSeq.value = it.data.data.competitionImageFileDto.imgSeq
                    getIsUserParticipants(competitionDto.value.competitionSeq, userSeq)
                    getCompetitionTotalRanking(competitionDto.value.competitionSeq)
                }else if(it is Result.Fail){
                    getBeforeStartCompetition()
                }else if(it is Result.Error){
                    _errorMsgEvent.postValue("잠시 후 시도해주세요.")
                }
            }
        }
    }

    private fun getBeforeStartCompetition(){
        viewModelScope.launch(Dispatchers.IO) {
            competitionRepository.getBeforeStartCompetition().collectLatest {
                if (it is Result.Success){
                    _competitionDto.value = it.data.data.competitionDto
                    _competitionImageSeq.value = it.data.data.competitionImageFileDto.imgSeq
                    getIsUserParticipants(competitionDto.value.competitionSeq, userSeq)
                    getCompetitionTotalRanking(competitionDto.value.competitionSeq)
                }else if(it is Result.Error){
                    _errorMsgEvent.postValue("잠시 후 시도해주세요.")
                }
            }
        }
    }

    private fun getIsUserParticipants(competitionSeq: Int, userSeq: Int){
        viewModelScope.launch(Dispatchers.IO) {
            competitionRepository.getIsUserParticipants(competitionSeq, userSeq).collectLatest {
                if(it is Result.Success){
                    _participants.value = !it.data.data
                }else if(it is Result.Error){
                    _errorMsgEvent.postValue("잠시 후 시도해주세요.")
                }
            }
        }
    }

    fun joinCompetition(){
        viewModelScope.launch(Dispatchers.IO) {
            competitionRepository.joinCompetition(competitionDto.value.competitionSeq).collectLatest {
                if(it is Result.Success){
                    _successMsgEvent.postValue("신청 완료")
                    _participants.value = true
                }else if(it is Result.Error){
                    _errorMsgEvent.postValue("잠시 후 시도해주세요.")
                }
            }
        }
    }

    private fun getCompetitionTotalRanking(competitionSeq: Int){
        viewModelScope.launch(Dispatchers.IO) {
            competitionRepository.getCompetitionTotalRanking(competitionSeq, 3).collectLatest {
                if(it is Result.Success){
                    val rankingList = arrayListOf<RankingResponse>()
                    rankingList.addAll(it.data.data)

                    while (rankingList.size < 3){
                        rankingList.add(RankingResponse("", 0, 0, 0, 0, ""))
                    }

                    _rankingFirst.value = rankingList.get(0)
                    _rankingSecond.value = rankingList.get(1)
                    _rankingThird.value = rankingList.get(2)

                }else if(it is Result.Error){
                    _errorMsgEvent.postValue("잠시 후 시도해주세요.")
                }
            }
        }
    }

}