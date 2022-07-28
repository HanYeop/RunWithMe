package com.ssafy.runwithme.api

import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.response.MyProfileResponse
import com.ssafy.runwithme.model.response.MyTotalRecordResponse
import com.ssafy.runwithme.model.response.RankingResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

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