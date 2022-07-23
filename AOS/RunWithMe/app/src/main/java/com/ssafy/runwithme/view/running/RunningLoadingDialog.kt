package com.ssafy.runwithme.view.running

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.ssafy.runwithme.R
import com.ssafy.runwithme.databinding.DialogRunningLoadingBinding
import com.ssafy.runwithme.utils.dialogResize

class RunningLoadingDialog (context: Context): Dialog(context) {

    private lateinit var binding: DialogRunningLoadingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_running_loading,
            null,
            false
        )
        setContentView(binding.root)

        context.dialogResize(this, 0.8f, 0.5f)

        // 배경 투명하게 바꿔줌
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 취소 불가능
        setCancelable(false)
    }
}