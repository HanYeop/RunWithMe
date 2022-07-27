package com.ssafy.runwithme.view.crew_recruit.create

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.ssafy.runwithme.R
import com.ssafy.runwithme.databinding.DialogGoalDaysBinding
import com.ssafy.runwithme.databinding.DialogMaxMemberBinding
import com.ssafy.runwithme.utils.dialogResize

class MaxMemberDialog(context: Context, private val listener : MaxMemberDialogListener): Dialog(context) {

    private lateinit var binding: DialogMaxMemberBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_max_member,
            null,
            false
        )
        setContentView(binding.root)


        binding.apply {
            numberpickerMaxMember.minValue = 2
            numberpickerMaxMember.maxValue = 20
            numberpickerMaxMember.value = 7
//            numberpickerGoalAmount.displayedValues = goal_amount_values
            //순환 안되게 막기
            numberpickerMaxMember.wrapSelectorWheel = false
        }

        context.dialogResize(this, 0.8f, 0.5f)

        // 배경 투명하게 바꿔줌
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        initClickListener()
    }

    private fun initClickListener() {
        binding.apply {
            tvPositive.setOnClickListener {
                val max = numberpickerMaxMember.value
                listener.onItemClick(max)
                dismiss()
            }
        }
    }
}