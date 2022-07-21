package com.ssafy.runwithme.view.create_recommend

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.ssafy.runwithme.R
import com.ssafy.runwithme.databinding.DialogCreateRecommendBinding
import com.ssafy.runwithme.utils.dialogResize


class CreateRecommendDialog(context: Context): Dialog(context) {

    private lateinit var binding: DialogCreateRecommendBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_create_recommend, null, false)
        setContentView(binding.root)

        context.dialogResize(this,0.8f,0.5f)

        // 배경 투명하게 바꿔줌
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}