package com.ssafy.runwithme.api

import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.dto.AchievementDto
import com.ssafy.runwithme.model.response.MyAchieveResponse
import retrofit2.http.GET

interface AchieveApi {

    @GET("achieve/list")
    suspend fun getAchieveList(): BaseResponse<List<AchievementDto>>

    @GET("achieve/my")
    suspend fun getMyAchieve(): BaseResponse<List<MyAchieveResponse>>
}