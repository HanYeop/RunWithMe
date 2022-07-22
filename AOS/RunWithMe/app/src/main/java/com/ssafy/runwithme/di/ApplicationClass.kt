package com.ssafy.runwithme.di

import android.app.Application
import android.content.Context
import com.kakao.sdk.common.KakaoSdk
import com.ssafy.runwithme.R
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ApplicationClass: Application() {
    companion object {
        var appContext : Context? = null
    }
    override fun onCreate() {
        super.onCreate()
        appContext = this
        KakaoSdk.init(this,getString(R.string.kakao_app_key))
    }
}