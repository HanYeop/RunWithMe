package com.ssafy.runwithme.api

import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.dto.TrackBoardDto
import com.ssafy.runwithme.model.dto.TrackBoardFileDto
import com.ssafy.runwithme.model.response.RecommendResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface RecommendApi {

    @Multipart
    @POST("recommend/board")
    suspend fun createRecommend(
        @Part("trackBoardDto") trackBoardDto: RequestBody,
        @Part imgFile : MultipartBody.Part
    ): BaseResponse<TrackBoardDto>

    @GET("recommend/boards")
    suspend fun getRecommends(
        @Query("leftLng") leftLng: Double,
        @Query("lowerLat") lowerLat: Double,
        @Query("rightLng") rightLng: Double,
        @Query("upperLat") upperLat: Double
    ): BaseResponse<List<TrackBoardFileDto>>
}