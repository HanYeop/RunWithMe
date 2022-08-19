package com.ssafy.runwithme.api

import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.dto.ReportDto
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface CustomerCenterApi {

    @POST("customer-center/report/board/{boardSeq}")
    suspend fun reportBoard(@Path("boardSeq") boardSeq: Int, @Body report_content : String) : BaseResponse<ReportDto>

}