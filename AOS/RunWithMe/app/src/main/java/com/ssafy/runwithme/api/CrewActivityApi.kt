package com.ssafy.runwithme.api

import com.ssafy.runwithme.model.dto.CrewBoardResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CrewActivityApi {

    @GET("crew-activity/{crewId}/boards")
    suspend fun getCrewBoards(
        @Path("crewId") crewId: Int,
        @Query("size") size: Int,
        @Query("offset") offset: Int
    ): Response<List<CrewBoardResponse>>
}