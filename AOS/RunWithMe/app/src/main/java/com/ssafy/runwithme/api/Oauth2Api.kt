package com.ssafy.runwithme.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Oauth2Api {

    @GET("login/oauth2/code/google")
    suspend fun googleLogin(@Query("code") code: String, @Query("state") state: String)

    @GET("oauth2/authorization/naver")
    suspend fun naverLogin(@Query("token") token: String)

    @GET("oauth2/authorization/kakao")
    suspend fun kakaoLogin(@Path("token") token: String)
}