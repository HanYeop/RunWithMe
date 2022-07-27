package com.ssafy.runwithme.api

import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.dto.ImageFileDto
import com.ssafy.runwithme.model.dto.UserDto
import com.ssafy.runwithme.model.response.MyProfileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface MyActivityApi {

    @GET("my-activity/profile")
    suspend fun getMyProfile() : BaseResponse<MyProfileResponse>

    @Multipart
    @POST("my-activity/profile")
    suspend fun editMyProfile(
        @Part("profile") profileEditDto : RequestBody,
    ) : BaseResponse<MyProfileResponse>

    @Multipart
    @POST("my-activity/profile")
    suspend fun editMyProfile(
        @Part("profile") profileEditDto : RequestBody,
        @Part("imgFile") imgFile : MultipartBody.Part
    ) : BaseResponse<MyProfileResponse>
}