package com.ssafy.runwithme.datasource

import com.ssafy.runwithme.api.CompetitionApi
import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.response.CompetitionResponse
import com.ssafy.runwithme.model.response.RankingResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompetitionDataSource @Inject constructor(
    private val competitionApi: CompetitionApi
){

    // 진행 중인 대회 조회
    fun getInprogressCompetition() : Flow<BaseResponse<CompetitionResponse>> = flow {
        emit(competitionApi.getInprogressCompetition())
    }

    // 시작 전인 대회 조회
    fun getBeforeStartCompetition() : Flow<BaseResponse<CompetitionResponse>> = flow {
        emit(competitionApi.getBeforeStartCompetition())
    }

    fun getIsUserParticipants(competitionSeq: Int, userSeq: Int) : Flow<BaseResponse<Boolean>> = flow {
        emit(competitionApi.getIsUserParticipants(competitionSeq, userSeq))
    }

    fun getCompetitionTotalRanking(competitionSeq: Int) : Flow<BaseResponse<List<RankingResponse>>> = flow {
        emit(competitionApi.getCompetitionTotalRanking(competitionSeq))
    }

    fun getCompetitionTotalRanking(competitionSeq: Int, size: Int) : Flow<BaseResponse<List<RankingResponse>>> = flow {
        emit(competitionApi.getCompetitionTotalRanking(competitionSeq, size))
    }

    fun getCompetitionMyRanking(competitionSeq: Int, userSeq: Int) : Flow<BaseResponse<RankingResponse>> = flow {
        emit(competitionApi.getCompetitionMyRanking(competitionSeq, userSeq))
    }

    fun joinCompetition(competitionSeq: Int): Flow<BaseResponse<Boolean>> = flow {
        emit(competitionApi.joinCompetition(competitionSeq))
    }

}