package com.ssafy.runwithme.api

import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.response.RankingResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TotalRankingApi {

    @GET("total-ranking/{type}")
    suspend fun getTotalRanking(
        @Path("type") rankingType : String,
        @Query("size") size : Int,
        @Query("offset") offset : Int,
    ) : BaseResponse<List<RankingResponse>>

    @GET("total-ranking/my/{type}")
    suspend fun getMyRanking(
        @Path("type") rankingType: String
    ) : BaseResponse<RankingResponse>

}