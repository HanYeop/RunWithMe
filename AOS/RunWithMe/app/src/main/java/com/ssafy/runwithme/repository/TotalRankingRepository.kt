package com.ssafy.runwithme.repository

import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.datasource.TotalRankingRemoteDataSource
import com.ssafy.runwithme.datasource.UserRemoteDataSource
import com.ssafy.runwithme.model.dto.UserDto
import com.ssafy.runwithme.model.response.JoinResponse
import com.ssafy.runwithme.model.response.RankingResponse
import com.ssafy.runwithme.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TotalRankingRepository @Inject constructor(
    private val totalRankingRemoteDataSource : TotalRankingRemoteDataSource
){

    fun getTotalRanking(type : String, size : Int, offset : Int): Flow<Result<BaseResponse<List<RankingResponse>>>> = flow {
        emit(Result.Loading)
        totalRankingRemoteDataSource.getTotalRanking(type, size, offset).collect {
            if(it.success){
                emit(Result.Success(it))
            } else if (!it.success){
                emit(Result.Fail(it))
            } else {
                emit(Result.Empty)
            }
        }
    }.catch { e ->
        emit(Result.Error(e))
    }

    fun getMyRanking(type : String): Flow<Result<BaseResponse<RankingResponse>>> = flow {
        emit(Result.Loading)
        totalRankingRemoteDataSource.getMyRanking(type).collect {
            if(it.success){
                emit(Result.Success(it))
            } else if (!it.success){
                emit(Result.Fail(it))
            } else {
                emit(Result.Empty)
            }
        }
    }.catch { e ->
        emit(Result.Error(e))
    }
}