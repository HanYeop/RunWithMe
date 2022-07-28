package com.ssafy.runwithme.api

import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.dto.TrackBoardDto
import retrofit2.http.POST
import retrofit2.http.Query

interface RecommendApi {

    @POST("recommend/board")
    suspend fun createRecommend(
        @Query("environment_point") environmentPoint: Int,
        @Query("hard_point") hardPoint: Int,
        @Query("run_record_seq") RunRecordSeq: Int
    ): BaseResponse<TrackBoardDto>
}