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
import com.ssafy.runwithme.databinding.DialogPurposeTimeBinding
import com.ssafy.runwithme.utils.dialogResize

class PurposeTimeDialog (context: Context, private val listener : PurposeTimeDialogListener): Dialog(context) {

    private lateinit var binding: DialogPurposeTimeBinding
    private lateinit var timeValues : Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_purpose_time,
            null,
            false
        )
        setContentView(binding.root)

        val time_list = arrayListOf<String>()

        for(i in 1..60){
            time_list.add((10 * i).toString())
        }

        timeValues = time_list.toTypedArray()

        binding.apply {
            numberpickerTime.minValue = 0
            numberpickerTime.maxValue = 59
            numberpickerTime.value = 4
            numberpickerTime.displayedValues = timeValues
            //순환 안되게 막기
            numberpickerTime.wrapSelectorWheel = false

            numberpickerTime.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        }

//        context.dialogResize(this, 0.9f, 0.5f)

        // 배경 투명하게 바꿔줌
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        initClickListener()
    }

    private fun initClickListener() {
        binding.apply {
            tvPositive.setOnClickListener {
                val time = timeValues[numberpickerTime.value]
                listener.onItemClick(time)
                dismiss()
            }
        }
    }

}