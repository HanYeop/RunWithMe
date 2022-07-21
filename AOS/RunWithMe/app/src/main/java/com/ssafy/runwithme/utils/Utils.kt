package com.ssafy.runwithme.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.view.WindowManager

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