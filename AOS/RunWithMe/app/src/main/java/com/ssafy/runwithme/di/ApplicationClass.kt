package com.ssafy.runwithme.di

import android.app.Application
import android.content.Context
import com.kakao.sdk.common.KakaoSdk
import com.navercorp.nid.NaverIdLoginSDK
import com.ssafy.runwithme.R
import dagger.hilt.android.HiltAndroidApp
import java.io.InputStream
import java.security.KeyStore
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManagerFactory

@HiltAndroidApp
class ApplicationClass: Application() {
    companion object {
        var appContext : Context? = null

        fun getTrustManagerFactory(context: Context): TrustManagerFactory {
            // 1. CA 로드
            val cf: CertificateFactory = CertificateFactory.getInstance("X.509")
            val caInput: InputStream = context.resources.openRawResource(R.raw.fullchain)
            val ca: X509Certificate = caInput.use {
                cf.generateCertificate(it) as X509Certificate
            }

            // 2. 신뢰할 수있는 CA를 포함하는 키 스토어 생성
            val keyStoreType = KeyStore.getDefaultType()
            val keyStore = KeyStore.getInstance(keyStoreType).apply {
                load(null, null)
                setCertificateEntry("ca", ca)
            }

            // 3. CA 입력을 신뢰하는 TrustManager 생성
            val tmfAlgorithm: String = TrustManagerFactory.getDefaultAlgorithm()
            val tmf: TrustManagerFactory = TrustManagerFactory.getInstance(tmfAlgorithm).apply {
                init(keyStore)
            }
            return tmf
        }

        fun getSSLSocketFactory(
            tmf: TrustManagerFactory
        ): SSLSocketFactory {
            //4. TrustManager를 사용하는 SSLContext 생성
            val sslContext: SSLContext = SSLContext.getInstance("TLS")
            sslContext.init(null, tmf.trustManagers, null)
            return sslContext.socketFactory
        }
    }
    override fun onCreate() {
        super.onCreate()
        appContext = this
        KakaoSdk.init(this,getString(R.string.kakao_app_key))

        NaverIdLoginSDK.initialize(this, getString(R.string.naver_client_id_key),
            getString(R.string.naver_secret_key), getString(R.string.app_name))
    }
}