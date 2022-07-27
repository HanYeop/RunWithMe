package com.ssafy.runwithme.view.crew_recruit.create

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.ssafy.runwithme.R
import com.ssafy.runwithme.databinding.DialogWeeksBinding
import com.ssafy.runwithme.utils.dialogResize

class GoalWeeksDialog(context: Context, private val listener : GoalWeeksDialogListener): Dialog(context) {

    private lateinit var binding: DialogWeeksBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_weeks,
            null,
            false
        )
        setContentView(binding.root)


        binding.apply {
            numberpickerWeeks.minValue = 1
            numberpickerWeeks.maxValue = 25
//            numberpickerGoalAmount.displayedValues = goal_amount_values
        //순환 안되게 막기
        //            numberpickerGoalAmount.wrapSelectorWheel = false
        }

        context.dialogResize(this, 0.8f, 0.5f)

        // 배경 투명하게 바꿔줌
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        initClickListener()
    }

    private fun initClickListener() {
        binding.apply {
            tvPositive.setOnClickListener {
                val amount = numberpickerWeeks.value
                listener.onItemClick(amount)
                dismiss()
            }
        }
    }
}