package com.ssafy.runwithme.utils

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Point
import android.os.Build
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.ssafy.runwithme.R
import com.ssafy.runwithme.binding.ViewBindingAdapter.setRecordImage
import java.text.SimpleDateFormat
import java.util.*

// 다이얼로그 사이즈 조절
fun Context.dialogResize(dialog: Dialog, width: Float, height: Float){
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

    if (Build.VERSION.SDK_INT < 30){
        val display = windowManager.defaultDisplay
        val size = Point()

        display.getSize(size)

        val window = dialog.window

        val x = (size.x * width).toInt()
        val y = (size.y * height).toInt()

        window?.setLayout(x, y)

    }else{
        val rect = windowManager.currentWindowMetrics.bounds

        val window = dialog.window
        val x = (rect.width() * width).toInt()
        val y = (rect.height() * height).toInt()

        window?.setLayout(x, y)
    }
}

// 서버 시간 포매터
fun timeFormatter(time: Long?): String {
    if(time == null){
        return ""
    }
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    return dateFormat.format(time)
}

// 러닝 제목 포매터
fun timeNameFormatter(time: Long?): String {
    if(time == null){
        return ""
    }
    val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일 러닝")

    return dateFormat.format(time)
}

// 러닝 시작 할 때 위 정보를 저장하고 있어야함. 백그라운드에서 동작하여 초기화 가능성 있으므로. sharedPreferences
fun runningStart(sharedPreferences: SharedPreferences, crewId: Int, crewName: String, goalType: String, goalAmount: Int, recordSeq: Int){
    sharedPreferences.edit().putLong(RUN_RECORD_START_TIME, System.currentTimeMillis()).apply()
    sharedPreferences.edit().putInt(RUN_RECORD_CREW_ID, crewId).apply()
    sharedPreferences.edit().putString(RUN_RECORD_CREW_NAME, crewName).apply()
    sharedPreferences.edit().putString(RUN_GOAL_TYPE, goalType).apply()
    sharedPreferences.edit().putInt(RUN_GOAL_AMOUNT, goalAmount).apply()
    sharedPreferences.edit().putInt(RUN_SCRAP_RECORD_SEQ, recordSeq).apply()
}

// 러닝 결과 시간 표기 포매터
fun startEndFormatter(startTime: Long, endTime: Long): String {
    val dateFormat = SimpleDateFormat("HH:mm")
    val start = dateFormat.format(startTime)
    val end = dateFormat.format(endTime)
    return "$start - $end"
}

fun ImageView.imageFormatter(imageSeq: Int){
    Glide.with(this.context).load("${BASE_URL}images/${imageSeq}")
        .placeholder(R.drawable.img)
        .into(this)
}

