package com.ssafy.runwithme.repository

import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.datasource.CompetitionDataSource
import com.ssafy.runwithme.model.dto.CreateCrewBoardDto
import com.ssafy.runwithme.model.response.CompetitionResponse
import com.ssafy.runwithme.model.response.CrewBoardResponse
import com.ssafy.runwithme.model.response.RankingResponse
import com.ssafy.runwithme.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompetitionRepository @Inject constructor(
    private val competitionDataSource: CompetitionDataSource
) {

    // 진행 중인 대회 조회
    fun getInprogressCompetition() : Flow<Result<BaseResponse<CompetitionResponse>>> = flow {
        emit(Result.Loading)
        competitionDataSource.getInprogressCompetition().collect {
            if(it.success){
                emit(Result.Success(it))
            }else if(!it.success){
                emit(Result.Fail(it))
            }
        }
    } .catch { e ->
        emit(Result.Error(e))
    }

    // 시작 전인 대회 조회
    fun getBeforeStartCompetition() : Flow<Result<BaseResponse<CompetitionResponse>>> = flow {
        emit(Result.Loading)
        competitionDataSource.getBeforeStartCompetition().collect {
            if(it.success){
                emit(Result.Success(it))
            }else if(!it.success){
                emit(Result.Fail(it))
            }
        }
    } .catch { e ->
        emit(Result.Error(e))
    }

    fun getIsUserParticipants(competitionSeq: Int, userSeq: Int): Flow<Result<BaseResponse<Boolean>>> = flow {
        emit(Result.Loading)
        competitionDataSource.getIsUserParticipants(competitionSeq, userSeq).collect {
            if(it.success){
                emit(Result.Success(it))
            }else if(!it.success){
                emit(Result.Fail(it))
            }
        }
    } .catch { e ->
        emit(Result.Error(e))
    }

    fun getCompetitionTotalRanking(competitionSeq: Int): Flow<Result<BaseResponse<List<RankingResponse>>>> = flow {
        emit(Result.Loading)
        competitionDataSource.getCompetitionTotalRanking(competitionSeq).collect {
            if(it.success){
                emit(Result.Success(it))
            }else if(!it.success){
                emit(Result.Fail(it))
            }else{
                emit(Result.Empty)
            }
        }
    } .catch { e ->
        emit(Result.Error(e))
    }

    fun getCompetitionTotalRanking(competitionSeq: Int, size: Int): Flow<Result<BaseResponse<List<RankingResponse>>>> = flow {
        emit(Result.Loading)
        competitionDataSource.getCompetitionTotalRanking(competitionSeq, size).collect {
            if(it.success){
                emit(Result.Success(it))
            }else if(!it.success){
                emit(Result.Fail(it))
            }
        }
    } .catch { e ->
        emit(Result.Error(e))
    }

    fun getCompetitionMyRanking(competitionSeq: Int, userSeq: Int): Flow<Result<BaseResponse<RankingResponse>>> = flow {
        emit(Result.Loading)
        competitionDataSource.getCompetitionMyRanking(competitionSeq, userSeq).collect {
            if(it.success){
                emit(Result.Success(it))
            }else if(!it.success){
                emit(Result.Fail(it))
            }
        }
    } .catch { e ->
        emit(Result.Error(e))
    }

    fun joinCompetition(competitionSeq: Int): Flow<Result<BaseResponse<Boolean>>> = flow {
        emit(Result.Loading)
        competitionDataSource.joinCompetition(competitionSeq).collect {
            if(it.success){
                emit(Result.Success(it))
            }else if(!it.success){
                emit(Result.Fail(it))
            }
        }
    } .catch { e ->
        emit(Result.Error(e))
    }

}