package com.ssafy.runwithme.api

import com.ssafy.runwithme.model.response.OauthResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface Oauth2Api {

    @GET("login/oauth2/code/google")
    suspend fun googleLogin(@Query("code") code: String): OauthResponse

    @GET("login/oauth2/code/naver")
    suspend fun naverLogin(@Query("code") code: String, @Query("email") email: String): OauthResponse

    @GET("login/oauth2/code/kakao")
    suspend fun kakaoLogin(@Query("code") code: String): OauthResponse
}