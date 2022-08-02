package com.ssafy.runwithme.api

import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.dto.FcmTokenDto
import com.ssafy.runwithme.model.dto.UserDto
import com.ssafy.runwithme.model.response.JoinResponse
import com.ssafy.runwithme.utils.JWT
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface UserApi {

    @POST("user/profile")
    suspend fun joinUser(
        @Header(JWT) token: String,
        @Body userDto: UserDto
    ): BaseResponse<JoinResponse>

    @POST("user/fcm-token")
    suspend fun fcmToken(
        @Body fcmTokenDto: FcmTokenDto
    ): BaseResponse<String>
}