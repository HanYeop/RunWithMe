package com.ssafy.runwithme.api

import com.ssafy.runwithme.model.response.OauthResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Oauth2Api {

    @GET("login/oauth2/code/google")
    suspend fun googleLogin(@Query("code") code: String): Response<OauthResponse>

//    @GET("login/oauth2/code/google")
//    suspend fun naverLogin(@Query("token") token: String)
//
//    @GET("oauth2/authorization/kakao")
//    suspend fun kakaoLogin(@Path("token") token: String)
}