package com.ssafy.runwithme.api

import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.dto.UserDto
import com.ssafy.runwithme.model.response.MyProfileResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MyActivityApi {

    @GET("my-activity/profile")
    suspend fun getMyProfile() : BaseResponse<MyProfileResponse>

    @POST("my-activity/profile")
    suspend fun editMyProfile(
        @Body userDto: UserDto
    ) : BaseResponse<MyProfileResponse>
}