package com.ssafy.runwithme.di

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.ssafy.runwithme.MainActivity
import com.ssafy.runwithme.R
import com.ssafy.runwithme.utils.NOTIFICATION_CHANNEL_ID
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {

    // FusedLocationProviderClient DI
    @ServiceScoped
    @Provides
    fun providesFusedLocationProviderClient(
        @ApplicationContext context : Context
    ) = FusedLocationProviderClient(context)

    // PendingIntent DI
    @ServiceScoped
    @Provides
    fun provideMainActivityPendingIntent(
        @ApplicationContext context : Context
    ) = PendingIntent.getActivity(
        context,0,
        Intent(context, MainActivity::class.java).also {
            it.action = Intent.ACTION_MAIN
            it.addCategory(Intent.CATEGORY_LAUNCHER)
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }, PendingIntent.FLAG_MUTABLE
    )!!

    // NotificationCompat.Builder DI
    @ServiceScoped
    @Provides
    fun provideBaseNotificationBuilder(
        @ApplicationContext context: Context,
        pendingIntent: PendingIntent
    ) = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
        .setAutoCancel(false)
        .setOngoing(true)
        .setSmallIcon(R.drawable.img)
        .setContentTitle("달리기 기록을 측정중입니다.")
        .setContentText("00:00:00")
        .setContentIntent(pendingIntent)
}