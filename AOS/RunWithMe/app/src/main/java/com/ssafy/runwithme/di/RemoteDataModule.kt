package com.ssafy.runwithme.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ssafy.runwithme.api.Oauth2Api
import com.ssafy.runwithme.utils.BASE_URL
import com.ssafy.runwithme.utils.XAccessTokenInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteDataModule {

    // HttpLoggingInterceptor DI
    @Provides
    @Singleton
    fun provideInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    // OkHttpClient DI
    @Provides
    @Singleton
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor
    ,xAccessTokenInterceptor: XAccessTokenInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
//            .addNetworkInterceptor(xAccessTokenInterceptor)
            .addInterceptor(httpLoggingInterceptor)
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
    fun provideRetrofitInstance(gson: Gson, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }

    // Oauth2Api DI
    @Provides
    @Singleton
    fun provideOauth2Api(retrofit: Retrofit): Oauth2Api {
        return retrofit.create(Oauth2Api::class.java)
    }
}