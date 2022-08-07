package com.ssafy.runwithme.api

import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.dto.ScrapInfoDto
import retrofit2.http.*

interface ScrapApi {

    @POST("scrap/{trackBoardSeq}")
    suspend fun addMyScrap(
        @Path("trackBoardSeq") trackBoardSeq : Int,
        @Query("title") title : String
    ): BaseResponse<ScrapInfoDto>

    @GET("scrap/")
    suspend fun getMyScrap(
        @Query("title") title : String
    ): BaseResponse<List<ScrapInfoDto>>

    @DELETE("scrap/{scrapSeq}")
    suspend fun deleteMyScrap(
        @Path("scrapSeq") scrapSeq : Int
    ) : BaseResponse<Boolean>

}