package com.ssafy.runwithme.view.crew_recruit.create

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.ssafy.runwithme.R
import com.ssafy.runwithme.databinding.DialogEndTimeBinding
import com.ssafy.runwithme.utils.dialogResize

class EndTimeDialog (context: Context, private val listener : EndTimeDialogListener): Dialog(context) {

    private lateinit var binding: DialogEndTimeBinding
    private lateinit var endMinuteValues : Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_end_time,
            null,
            false
        )
        setContentView(binding.root)

        endMinuteValues = arrayOf("00", "30")

        binding.apply {
            numberpickerHour.minValue = 0
            numberpickerHour.maxValue = 23
            numberpickerHour.value = 21
            //순환 안되게 막기
            numberpickerHour.wrapSelectorWheel = false

            numberpickerMinute.minValue = 0
            numberpickerMinute.maxValue = 1
            numberpickerMinute.displayedValues = endMinuteValues

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
                val minute = endMinuteValues[numberpickerMinute.value]
                listener.onItemClick(hour, minute)
                dismiss()
            }
        }
    }

}