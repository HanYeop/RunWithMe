package com.ssafy.runwithme.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.ssafy.runwithme.db.RunDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // DB DI
    @Singleton
    @Provides
    fun provideRunDatabase(app : Application) =
        Room.databaseBuilder(app, RunDatabase::class.java,"run_db")
            .fallbackToDestructiveMigration() // 버전 변경 시 기존 데이터 삭제
            .build()

    // Dao DI
    @Singleton
    @Provides
    fun provideRunDao(db : RunDatabase) = db.runDao()

    // SharedPreferences DI
    @Singleton
    @Provides
    fun provideSharedPreferences(app: Application) =
        app.getSharedPreferences("app_preference", Context.MODE_PRIVATE)!!
}