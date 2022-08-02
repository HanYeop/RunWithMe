package com.ssafy.runwithme.service

import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ssafy.runwithme.MainActivity
import com.ssafy.runwithme.R
import com.ssafy.runwithme.utils.FCM_CHANNEL_ID
import com.ssafy.runwithme.utils.TAG

class MyFirebaseMessagingService : FirebaseMessagingService() {

    // 새로운 토큰이 생성될 때 마다 해당 콜백이 호출된다.
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // 새로운 토큰 로그 남기기
        Log.d(TAG, "onNewToken: $token")
    }

    // Foreground에서 Push Service를 받기위해 Notification 설정
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "onMessageReceived: ")

        remoteMessage.notification.let { message ->
            val messageTitle = message!!.title
            val messageContent = message!!.body

            val mainIntent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

            val mainPendingIntent =
                PendingIntent.getActivity(this, 0, mainIntent, PendingIntent.FLAG_IMMUTABLE)

            val builder = NotificationCompat.Builder(this, FCM_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(messageTitle)
                .setContentText(messageContent)
                .setAutoCancel(true)
                .setContentIntent(mainPendingIntent)

            NotificationManagerCompat.from(this).apply {
                notify(101, builder.build())
            }
        }
    }
}