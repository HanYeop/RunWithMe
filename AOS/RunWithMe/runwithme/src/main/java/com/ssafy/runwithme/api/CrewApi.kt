package com.ssafy.runwithme.api

import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.dto.CoordinateDto
import com.ssafy.runwithme.model.dto.CrewDto
import com.ssafy.runwithme.model.dto.PasswordDto
import com.ssafy.runwithme.model.response.CreateRunRecordResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface CrewApi {
    @Multipart
    @POST("crew/{crewId}/records")
    suspend fun createRunRecords(
        @Path("crewId") crewId: Int,
        @Part("runRecord") runRecordDto: RequestBody,
        @Part imgFile: MultipartBody.Part,
    ): BaseResponse<CreateRunRecordResponse>

    @POST("crew/records/{recordseq}/coordinate")
    suspend fun createCoordinates(
        @Path("recordseq") recordSeq: Int,
        @Body coordinates: List<CoordinateDto>
    ): BaseResponse<String>

}