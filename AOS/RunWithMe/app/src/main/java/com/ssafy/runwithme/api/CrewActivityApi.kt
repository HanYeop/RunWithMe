package com.ssafy.runwithme.api

import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.response.CrewBoardResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CrewActivityApi {

    // Page 단위
    @GET("crew-activity/{crewSeq}/boards")
    suspend fun getCrewBoards(
        @Path("crewSeq") crewSeq: Int,
        @Query("offset") offset: Int,
        @Query("size") size: Int,
    ): BaseResponse<List<CrewBoardResponse>>
}