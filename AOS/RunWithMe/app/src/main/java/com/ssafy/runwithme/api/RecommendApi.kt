package com.ssafy.runwithme.api

import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.dto.TrackBoardDto
import com.ssafy.runwithme.model.response.RecommendResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RecommendApi {

    @POST("recommend/board")
    suspend fun createRecommend(
        @Query("environment_point") environmentPoint: Int,
        @Query("hard_point") hardPoint: Int,
        @Query("run_record_seq") RunRecordSeq: Int
    ): BaseResponse<TrackBoardDto>

    @GET("recommend/boards")
    suspend fun getRecommends(
        @Query("leftLng") leftLng: Double,
        @Query("lowerLat") lowerLat: Double,
        @Query("rightLng") rightLng: Double,
        @Query("upperLat") upperLat: Double
    ): BaseResponse<List<RecommendResponse>>
}