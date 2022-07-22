package com.ssafy.runwithme.utils

import android.content.SharedPreferences
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject


class XAccessTokenInterceptor @Inject constructor(
    private val sharedPref: SharedPreferences
): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var token = runBlocking {
            sharedPref.getString(JWT,"")!!
        }
        val request = chain.request().newBuilder()
            .addHeader(JWT, token)
            .build()
        return chain.proceed(request)
    }
}