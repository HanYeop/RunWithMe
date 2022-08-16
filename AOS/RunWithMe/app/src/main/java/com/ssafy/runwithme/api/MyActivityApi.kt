package com.ssafy.runwithme.api

import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.dto.CrewBoardDto
import com.ssafy.runwithme.model.dto.RunRecordDto
import com.ssafy.runwithme.model.response.CrewBoardResponse
import com.ssafy.runwithme.model.response.MyProfileResponse
import com.ssafy.runwithme.model.response.MyTotalRecordResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface MyActivityApi {

    @GET("my-activity/activity")
    suspend fun getMyRunRecord(
        // @Query("month") month : Int,
        // @Query("year") year : Int
    ) : BaseResponse<List<RunRecordDto>>

    @GET("my-activity/boards")
    suspend fun getMyBoards(
        @Query("size") size: Int,
        @Query("boardMaxSeq") boardMaxSeq: Int
    ) : BaseResponse<List<CrewBoardResponse>>

    @GET("my-activity/total-activity")
    suspend fun getMyTotalRecord() : BaseResponse<MyTotalRecordResponse>

    @GET("my-activity/profile")
    suspend fun getMyProfile() : BaseResponse<MyProfileResponse>

    @Multipart
    @POST("my-activity/profile")
    suspend fun editMyProfile(
        @Part("profile") profileEditDto : RequestBody,
        @Part imgFile : MultipartBody.Part?
    ) : BaseResponse<MyProfileResponse>

    @Multipart
    @POST("my-activity/profile")
    suspend fun editMyProfile(
        @Part("profile") profileEditDto : RequestBody,
    ) : BaseResponse<MyProfileResponse>

    @GET("my-activity/runabletoday/{crewSeq}")
    suspend fun runAbleToday(
        @Path("crewSeq") crewSeq: Int
    ): BaseResponse<Boolean>
}