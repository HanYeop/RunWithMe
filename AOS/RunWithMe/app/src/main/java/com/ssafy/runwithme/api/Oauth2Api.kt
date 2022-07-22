package com.ssafy.runwithme.api

import retrofit2.http.GET

interface Oauth2Api {

    @GET("oauth2/authorization/google")
    suspend fun googleLogin(): String
}