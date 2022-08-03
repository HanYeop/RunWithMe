package com.ssafy.runwithme.api

import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.response.MyProfileResponse
import com.ssafy.runwithme.model.response.MyRunRecordResponse
import com.ssafy.runwithme.model.response.MyTotalBoardsResponse
import com.ssafy.runwithme.model.response.MyTotalRecordResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface MyActivityApi {

    @GET("my-activity/activity")
    suspend fun getMyRunRecord(
        @Query("month") month : Int,
        @Query("year") year : Int
    ) : BaseResponse<List<MyRunRecordResponse>>

    @GET("my-activity/boards")
    suspend fun getMyBoards(
        @Query("size") size: Int,
        @Query("boardMaxSeq") boardMaxSeq: Int
    ) : BaseResponse<List<MyTotalBoardsResponse>>

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
}