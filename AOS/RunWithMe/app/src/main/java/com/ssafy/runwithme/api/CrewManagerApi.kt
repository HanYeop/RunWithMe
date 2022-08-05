package com.ssafy.runwithme.api

import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.dto.EndCrewFileDto
import com.ssafy.runwithme.model.response.CreateCrewResponse
import com.ssafy.runwithme.model.response.MyCurrentCrewResponse
import com.ssafy.runwithme.model.response.RecruitCrewResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface CrewManagerApi {

    @GET("crew-manager/my-current-crew")
    suspend fun getMyCurrentCrew(): BaseResponse<List<MyCurrentCrewResponse>>

    @GET("crew-manager/my-end-crew")
    suspend fun getMyEndCrew(): BaseResponse<List<EndCrewFileDto>>

    @Multipart
    @POST("crew-manager/crew")
    suspend fun createCrew(@Part("crewDto") crewDto: RequestBody, @Part imgFile: MultipartBody.Part?): BaseResponse<CreateCrewResponse>

    @Multipart
    @POST("crew-manager/crew")
    suspend fun createCrew(@Part("crewDto") crewDto: RequestBody): BaseResponse<CreateCrewResponse>

    @GET("crew-manager/recruitment")
    suspend fun getRecruitCrew(@Query("maxCrewSeq") maxCrewSeq: Int, @Query("size") size: Int) : BaseResponse<List<RecruitCrewResponse>>

    @GET("crew-manager/recruitment")
    suspend fun getSearchResultCrew(@Query("maxCrewSeq") maxCrewSeq: Int, @Query("size") size: Int,
                               @Query("title") crewName: String?, @Query("startDay") startDate: String?, @Query("endDay") endDate: String?
                                ,@Query("startTime") startTime: String?, @Query("endTime") endTime: String?,
                               @Query("pointMin") minCost: Int, @Query("pointMax") maxCost: Int,
                               @Query("purposeMinValue") purposeMinValue: Int, @Query("purposeMaxValue") purposeMaxValue: Int,
                               @Query("goalMinDay") goalMinDay: Int, @Query("goalMaxDay") goalMaxDay: Int,
                               @Query("purposeType") goalType: String?
    ): BaseResponse<List<RecruitCrewResponse>>

    @GET("crew-manager/{crewSeq}/membercheck")
    suspend fun checkCrewMember(@Path("crewSeq") crewSeq: Int) : BaseResponse<Boolean>

    @DELETE("crew-manager/crew/{crewSeq}")
    suspend fun deleteCrew(@Path("crewSeq") crewSeq: Int) : BaseResponse<Boolean>

    @DELETE("crew-manager/crew/{crewSeq}/user")
    suspend fun resignCrew(@Path("crewSeq") crewSeq: Int) : BaseResponse<Int>

}