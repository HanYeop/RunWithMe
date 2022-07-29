package com.ssafy.runwithme.api

import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.dto.CreateCrewBoardDto
import com.ssafy.runwithme.model.dto.CrewBoardDto
import com.ssafy.runwithme.model.dto.RunRecordDto
import com.ssafy.runwithme.model.response.CrewBoardResponse
import retrofit2.Response
import retrofit2.http.*

interface CrewActivityApi {

    // 마지막 인덱스를 넘겨줘야함
    @GET("crew-activity/{crewSeq}/boards")
    suspend fun getCrewBoards(
        @Path("crewSeq") crewSeq: Int,
        @Query("maxCrewBoardSeq") maxCrewBoardSeq: Int,
        @Query("size") size: Int,
    ): BaseResponse<List<CrewBoardResponse>>

    @GET("crew-activity/{crewSeq}/boards")
    suspend fun getCrewBoardsTop3(
        @Path("crewSeq") crewSeq: Int,
        @Query("size") size: Int,
    ): BaseResponse<List<CrewBoardResponse>>

    @GET("crew-activity/{crewSeq_p}/records")
    suspend fun getCrewRecords(
        @Path("crewSeq_p") crewSeq: String,
        @Query("maxRunRecordSeq") maxRunRecordSeq: Int,
        @Query("size") size: Int
    ) : BaseResponse<List<RunRecordDto>>

    @GET("crew-activity/{crewSeq_p}/records")
    suspend fun getCrewRecordsTop3(
        @Path("crewSeq_p") crewSeq: String,
        @Query("size") size: Int
    ) : BaseResponse<List<RunRecordDto>>

    @POST("crew-activity/{crewSeq}/board")
    suspend fun createCrewBoard(@Path("crewSeq") crewSeq: Int, @Body crewBoardDto: CreateCrewBoardDto) : BaseResponse<CrewBoardResponse>

    @DELETE("crew-activity/{crewSeq}/boards/{boardSeq}")
    suspend fun deleteCrewBoard(@Path("crewSeq") crewSeq: Int, @Path("boardSeq") boardSeq: Int) : BaseResponse<Boolean>
}