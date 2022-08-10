package com.ssafy.runwithme.datasource

import com.ssafy.runwithme.api.TotalRankingApi
import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.response.RankingResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TotalRankingRemoteDataSource @Inject constructor(
    private val totalRankingApi: TotalRankingApi
){
    fun getTotalRanking(type: String, size: Int, offset : Int): Flow<BaseResponse<List<RankingResponse>>> = flow {
        emit(totalRankingApi.getTotalRanking(type, size, offset))
    }

    fun getMyRanking(type: String): Flow<BaseResponse<RankingResponse>> = flow {
        emit(totalRankingApi.getMyRanking(type))
    }
}