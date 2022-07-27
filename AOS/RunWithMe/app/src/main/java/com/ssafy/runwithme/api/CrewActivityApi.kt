package com.ssafy.runwithme.api

import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.response.CrewBoardResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CrewActivityApi {

    // 마지막 인덱스를 넘겨줘야함
    @GET("crew-activity/{crewSeq}/boards")
    suspend fun getCrewBoards(
        @Path("crewSeq") crewSeq: Int,
        @Query("maxCrewBoardSeq") maxCrewBoardSeq: Int,
        @Query("size") size: Int,
    ): BaseResponse<List<CrewBoardResponse>>
}