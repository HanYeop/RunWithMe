package com.ssafy.runwithme.view.crew_recruit

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.ssafy.runwithme.R
import com.ssafy.runwithme.databinding.DialogGoalDaysBinding
import com.ssafy.runwithme.utils.dialogResize

class GoalDaysDialog(context: Context, private val listener : GoalDaysDialogListener): Dialog(context) {

    private lateinit var binding: DialogGoalDaysBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_goal_days,
            null,
            false
        )
        setContentView(binding.root)


        binding.apply {
            numberpickerGoalDays.minValue = 1
            numberpickerGoalDays.maxValue = 7
            numberpickerGoalDays.wrapSelectorWheel = false
        }

        context.dialogResize(this, 0.8f, 0.5f)

        // 배경 투명하게 바꿔줌
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        initClickListener()
    }

    private fun initClickListener() {
        binding.apply {
            tvPositive.setOnClickListener {
                val days = numberpickerGoalDays.value
                listener.onItemClick(days)
                dismiss()
            }
        }
    }
}