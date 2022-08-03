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


class CreateRecommendDialog(context: Context, private val listener: CreateRecommendListener,
        private val imageSeq: Int): Dialog(context) {

    private lateinit var binding: DialogCreateRecommendBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_create_recommend, null, false)
        setContentView(binding.root)

        binding.imageSeq = imageSeq

        // 배경 투명하게 바꿔줌
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        initClickListener()
    }

    private fun initClickListener(){
        binding.apply {
            btnCancel.setOnClickListener {
                dismiss()
            }

            btnRecommend.setOnClickListener {
                listener.onBtnOkClicked(binding.ratingEnvironment.rating.toInt(), binding.ratingHard.rating.toInt())
                dismiss()
            }
        }
    }
}