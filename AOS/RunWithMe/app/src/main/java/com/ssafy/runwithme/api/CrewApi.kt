package com.ssafy.runwithme.api

import com.ssafy.runwithme.base.BaseResponse
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
}