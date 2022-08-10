package com.ssafy.runwithme.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ssafy.runwithme.api.*
import com.ssafy.runwithme.utils.BASE_URL
import com.ssafy.runwithme.utils.WEATHER_BASE_URL
import com.ssafy.runwithme.utils.XAccessTokenInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

@Module
@InstallIn(SingletonComponent::class)
object RemoteDataModule {

    // TrustManagerFactory DI
    @Provides
    @Singleton
    fun provideTrustManagerFactory(@ApplicationContext context : Context): TrustManagerFactory{
        return ApplicationClass.getTrustManagerFactory(context)
    }

    // SSLSocketFactory DI
    @Provides
    @Singleton
    fun provideSSLSocketFactory(tmf: TrustManagerFactory): SSLSocketFactory{
        return tmf.let {
            ApplicationClass.getSSLSocketFactory(it)
        }
    }

    // HostnameVerifier DI
    @Provides
    @Singleton
    fun provideHostnameVerifier(): HostnameVerifier{
        var VERIFY_DOMAIN = "i7d101.p.ssafy.io"
        return HostnameVerifier { _, session ->
            HttpsURLConnection.getDefaultHostnameVerifier().run {
                verify(VERIFY_DOMAIN, session)
            }
        }
    }

    // HttpLoggingInterceptor DI
    @Provides
    @Singleton
    fun provideInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    // OkHttpClient DI
    @Provides
    @Singleton
    fun provideOkHttpClient(xAccessTokenInterceptor: XAccessTokenInterceptor, tmf: TrustManagerFactory,
    sslSocket: SSLSocketFactory, hostnameVerifier: HostnameVerifier): OkHttpClient {
        return OkHttpClient.Builder()
            .hostnameVerifier(hostnameVerifier)
            .sslSocketFactory(sslSocket, tmf.trustManagers?.get(0) as X509TrustManager)
            .addNetworkInterceptor(xAccessTokenInterceptor)
            .build()
    }

    // Gson DI
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().setLenient().create()
    }

    // Retrofit DI
    @Provides
    @Singleton
    @Named("mainRetrofit")
    fun provideRetrofitInstance(gson: Gson, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }

    // WeatherRetrofit DI
    @Provides
    @Singleton
    @Named("weatherRetrofit")
    fun provideWeatherRetrofitInstance(gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(WEATHER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideWeatherApi(@Named("weatherRetrofit") retrofit : Retrofit) : WeatherApi {
        return retrofit.create(WeatherApi::class.java)
    }

    // Oauth2Api DI
    @Provides
    @Singleton
    fun provideOauth2Api(@Named("mainRetrofit") retrofit: Retrofit): Oauth2Api {
        return retrofit.create(Oauth2Api::class.java)
    }

    // CrewMangerApi DI
    @Provides
    @Singleton
    fun provideCrewMangerApi(@Named("mainRetrofit") retrofit: Retrofit): CrewManagerApi {
        return retrofit.create(CrewManagerApi::class.java)
    }

    // CrewActivityApi DI
    @Provides
    @Singleton
    fun provideCrewActivityApi(@Named("mainRetrofit") retrofit: Retrofit): CrewActivityApi {
        return retrofit.create(CrewActivityApi::class.java)
    }

    // UserApi DI
    @Provides
    @Singleton
    fun provideUserApi(@Named("mainRetrofit") retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }

    // CrewApi DI
    @Provides
    @Singleton
    fun provideCrewApi(@Named("mainRetrofit") retrofit: Retrofit): CrewApi {
        return retrofit.create(CrewApi::class.java)
    }

    // MyActivityApi DI
    @Provides
    @Singleton
    fun provideMyActivityApi(@Named("mainRetrofit") retrofit: Retrofit): MyActivityApi {
        return retrofit.create(MyActivityApi::class.java)
    }

    // RecommendApi DI
    @Provides
    @Singleton
    fun provideRecommendApi(
        @Named("mainRetrofit") retrofit: Retrofit): RecommendApi {
        return retrofit.create(RecommendApi::class.java)
    }

    // TotalRanking DI
    @Provides
    @Singleton
    fun provideTotalRankingApi(@Named("mainRetrofit") retrofit: Retrofit): TotalRankingApi {
        return retrofit.create(TotalRankingApi::class.java)
    }

    // AchieveApi DI
    @Provides
    @Singleton
    fun provideAchieveApi(@Named("mainRetrofit") retrofit: Retrofit): AchieveApi {
        return retrofit.create(AchieveApi::class.java)
    }

    // CustomerCenterApi DI
    @Provides
    @Singleton
    fun provideCustomerCenterApi(@Named("mainRetrofit") retrofit: Retrofit): CustomerCenterApi {
        return retrofit.create(CustomerCenterApi::class.java)
    }

    // ScrapApi DI
    @Provides
    @Singleton
    fun provideScrapApi(@Named("mainRetrofit") retrofit: Retrofit): ScrapApi {
        return retrofit.create(ScrapApi::class.java)
    }
}