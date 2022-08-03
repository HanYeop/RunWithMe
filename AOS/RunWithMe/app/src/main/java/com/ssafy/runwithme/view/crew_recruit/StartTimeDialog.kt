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
import com.ssafy.runwithme.databinding.DialogStartTimeBinding
import com.ssafy.runwithme.utils.dialogResize

class StartTimeDialog(context: Context, private val listener : StartTimeDialogListener): Dialog(context) {

    private lateinit var binding: DialogStartTimeBinding
    private lateinit var startTimeValues : Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_start_time,
            null,
            false
        )
        setContentView(binding.root)

        startTimeValues = arrayOf("00", "10", "20", "30", "40", "50")

        binding.apply {
            numberpickerHour.minValue = 0
            numberpickerHour.maxValue = 23
            numberpickerHour.value = 20
            //순환 안되게 막기
            numberpickerHour.wrapSelectorWheel = false

            numberpickerMinute.minValue = 0
            numberpickerMinute.maxValue = 5
            numberpickerMinute.displayedValues = startTimeValues

            numberpickerMinute.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            numberpickerHour.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

            numberpickerMinute.wrapSelectorWheel = false
        }

        context.dialogResize(this, 0.9f, 0.5f)

        // 배경 투명하게 바꿔줌
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        initClickListener()
    }

    private fun initClickListener() {
        binding.apply {
            tvPositive.setOnClickListener {
                val hourInt = numberpickerHour.value
                var hour = hourInt.toString()
                if(hourInt < 10){
                    hour = "0" + hour
                }
                val minute = startTimeValues[numberpickerMinute.value]
                listener.onItemClick(hour, minute)
                dismiss()
            }
        }
    }

}