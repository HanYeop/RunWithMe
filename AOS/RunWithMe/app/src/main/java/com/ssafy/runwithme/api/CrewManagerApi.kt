package com.ssafy.runwithme.api

import com.ssafy.runwithme.model.dto.MyCurrentCrewResponse
import retrofit2.http.GET

interface CrewManagerApi {

    @GET("crew-manager/my-current-crew")
    suspend fun getMyCurrentCrew(): List<MyCurrentCrewResponse>
}