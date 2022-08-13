package com.ssafy.runwithme.api

import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.response.CompetitionResponse
import com.ssafy.runwithme.model.response.RankingResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CompetitionApi {

    // 진행 중인 대회 조회
    @GET("competition/inprogress")
    suspend fun getInprogressCompetition() : BaseResponse<CompetitionResponse>

    // 시작 전인 대회 조회
    @GET("competition/beforestart")
    suspend fun getBeforeStartCompetition() : BaseResponse<CompetitionResponse>

    @GET("competition/{competitionSeq}/check/{userSeq}")
    suspend fun getIsUserParticipants(@Path("competitionSeq") competitionSeq: Int, @Path("userSeq") userSeq: Int) : BaseResponse<Boolean>

    @GET("competition/{competitionSeq}/ranking")
    suspend fun getCompetitionTotalRanking(@Path("competitionSeq") competitionSeq: Int)
        :BaseResponse<List<RankingResponse>>

    @GET("competition/{competitionSeq}/ranking")
    suspend fun getCompetitionTotalRanking(@Path("competitionSeq") competitionSeq: Int, @Query("size") size: Int)
            :BaseResponse<List<RankingResponse>>

    @GET("competition/{competitionSeq}/ranking/{userSeq}")
    suspend fun getCompetitionMyRanking(@Path("competitionSeq") competitionSeq: Int, @Path("userSeq") userSeq: Int)
        :BaseResponse<RankingResponse>

    @POST("competition/{competitionSeq}/join")
    suspend fun joinCompetition(@Path("competitionSeq") competitionSeq: Int) : BaseResponse<Boolean>

}