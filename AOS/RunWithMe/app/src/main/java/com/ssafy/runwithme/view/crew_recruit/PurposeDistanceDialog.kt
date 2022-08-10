package com.ssafy.runwithme.view.crew_recruit

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.NumberPicker
import androidx.databinding.DataBindingUtil
import com.ssafy.runwithme.R
import com.ssafy.runwithme.databinding.DialogPurposeDistanceBinding
import com.ssafy.runwithme.utils.dialogResize

class PurposeDistanceDialog(context: Context, private val listener : PurposeDistanceDialogListener, private val minValue : Int): Dialog(context) {

    private lateinit var binding: DialogPurposeDistanceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_purpose_distance,
            null,
            false
        )
        setContentView(binding.root)


        binding.apply {
            numberpickerDistance.minValue = minValue
            numberpickerDistance.maxValue = 60
//            numberpickerGoalAmount.displayedValues = goal_amount_values
            //순환 안되게 막기
            numberpickerDistance.wrapSelectorWheel = false
            numberpickerDistance.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        }

//        context.dialogResize(this, 0.8f, 0.5f)

        // 배경 투명하게 바꿔줌
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        initClickListener()
    }

    private fun initClickListener() {
        binding.apply {
            tvPositive.setOnClickListener {
                val amount = numberpickerDistance.value
                listener.onItemClick(amount)
                dismiss()
            }
        }
    }
}