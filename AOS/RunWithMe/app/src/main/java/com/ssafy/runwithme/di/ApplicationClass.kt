package com.ssafy.runwithme.di

import android.app.Application
import android.content.Context
import com.kakao.sdk.common.KakaoSdk
import com.navercorp.nid.NaverIdLoginSDK
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

        NaverIdLoginSDK.initialize(this, getString(R.string.naver_client_id_key),
            getString(R.string.naver_secret_key), getString(R.string.app_name))
    }
}