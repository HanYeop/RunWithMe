package com.ssafy.runwithme.api

import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.response.MyProfileResponse
import com.ssafy.runwithme.model.response.MyTotalRecordResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface MyActivityApi {

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
}