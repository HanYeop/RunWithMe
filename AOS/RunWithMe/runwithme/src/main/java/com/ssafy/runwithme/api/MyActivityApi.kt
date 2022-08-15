package com.ssafy.runwithme.api

import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.response.MyProfileResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface MyActivityApi {

    @GET("my-activity/runabletoday/{crewSeq}")
    suspend fun runAbleToday(
        @Path("crewSeq") crewSeq: Int
    ): BaseResponse<Boolean>

    @GET("my-activity/profile")
    suspend fun getMyProfile() : BaseResponse<MyProfileResponse>
}