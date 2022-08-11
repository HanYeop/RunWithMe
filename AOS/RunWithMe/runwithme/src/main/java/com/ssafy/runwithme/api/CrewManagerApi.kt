package com.ssafy.runwithme.api

import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.response.MyCurrentCrewResponse
import retrofit2.http.GET

interface CrewManagerApi {

    @GET("crew-manager/my-current-crew")
    suspend fun getMyCurrentCrew(): BaseResponse<List<MyCurrentCrewResponse>>

}