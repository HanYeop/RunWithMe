package com.ssafy.runwithme.view.running.result.achievement

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import com.ssafy.runwithme.R
import com.ssafy.runwithme.databinding.DialogAchievementBinding
import com.ssafy.runwithme.utils.dialogResize

class AchievementDialog(context: Context): Dialog(context) {

    private lateinit var binding: DialogAchievementBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_achievement,
            null,
            false
        )
        setContentView(binding.root)

        context.dialogResize(this, 0.9f, 0.25f)

        // 배경 투명하게 바꿔줌
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.apply {
            val anim = AnimationUtils.loadAnimation(context, R.anim.blink_animation)
            tvAchievementHeader.startAnimation(anim)
        }

        initClickListener()
    }

    private fun initClickListener(){
        binding.apply {
            btnOk.setOnClickListener {
                dismiss()
            }
        }
    }
}