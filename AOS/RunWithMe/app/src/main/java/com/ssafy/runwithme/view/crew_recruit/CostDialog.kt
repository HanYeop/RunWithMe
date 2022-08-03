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
import com.ssafy.runwithme.databinding.DialogCostBinding
import com.ssafy.runwithme.utils.dialogResize

class CostDialog (context: Context, private val listener : CostDialogListener): Dialog(context) {

    private lateinit var binding: DialogCostBinding
    private lateinit var costValues : Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_cost,
            null,
            false
        )
        setContentView(binding.root)

        val cost_list = arrayListOf<String>()

        for(i in 1..20){
            cost_list.add((5000 * i).toString())
        }

        costValues = cost_list.toTypedArray()

        binding.apply {
            numberpickerCost.minValue = 0
            numberpickerCost.maxValue = 19
            numberpickerCost.value = 1
            numberpickerCost.displayedValues = costValues
            //순환 안되게 막기
            numberpickerCost.wrapSelectorWheel = false

            numberpickerCost.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        }

        context.dialogResize(this, 0.9f, 0.5f)

        // 배경 투명하게 바꿔줌
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        initClickListener()
    }

    private fun initClickListener() {
        binding.apply {
            tvPositive.setOnClickListener {
                val cost = costValues[numberpickerCost.value]
                listener.onItemClick(cost)
                dismiss()
            }
        }
    }

}