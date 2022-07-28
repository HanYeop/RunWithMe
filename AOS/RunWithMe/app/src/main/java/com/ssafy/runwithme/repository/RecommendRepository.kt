package com.ssafy.runwithme.repository

import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.datasource.RecommendRemoteDataSource
import com.ssafy.runwithme.model.dto.TrackBoardDto
import com.ssafy.runwithme.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RecommendRepository @Inject constructor(
    private val recommendRemoteDataSource: RecommendRemoteDataSource
){
    fun createRecommend(environmentPoint: Int, hardPoint: Int, RunRecordSeq: Int): Flow<Result<BaseResponse<TrackBoardDto>>> = flow {
        emit(Result.Loading)
        recommendRemoteDataSource.createRecommend(environmentPoint, hardPoint, RunRecordSeq).collect {
            if(it.success){
                emit(Result.Success(it))
            }else if(!it.success){
                emit(Result.Success(it))
            }
        }
    }.catch { e->
        emit(Result.Error(e))
    }
}