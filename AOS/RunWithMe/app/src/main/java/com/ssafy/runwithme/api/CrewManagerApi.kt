package com.ssafy.runwithme.api

import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.dto.CrewDto
import com.ssafy.runwithme.model.response.CreateCrewResponse
import com.ssafy.runwithme.model.response.CreateRunRecordResponse
import com.ssafy.runwithme.model.response.MyCurrentCrewResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface CrewManagerApi {

    @GET("crew-manager/my-current-crew")
    suspend fun getMyCurrentCrew(): BaseResponse<List<MyCurrentCrewResponse>>

    @Multipart
    @POST("crew-manager/crew")
    suspend fun createCrew(@Part("crewDto") crewDto: RequestBody, @Part imgFile: MultipartBody.Part?): BaseResponse<CreateCrewResponse>

    @Multipart
    @POST("crew-manager/crew")
    suspend fun createCrew(@Part("crewDto") crewDto: RequestBody): BaseResponse<CreateCrewResponse>

}