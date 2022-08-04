package com.ssafy.runwithme.view.running.list.sheet.custom

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.ssafy.runwithme.R
import com.ssafy.runwithme.databinding.DialogPracticeCustomBinding
import com.ssafy.runwithme.utils.GOAL_TYPE_DISTANCE
import com.ssafy.runwithme.utils.GOAL_TYPE_TIME

class PracticeCustomDialog (context: Context, private val listener: PracticeCustomListener): Dialog(context) {

    private lateinit var binding: DialogPracticeCustomBinding
    private var currentType: String = GOAL_TYPE_TIME

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_practice_custom,
            null,
            false
        )
        setContentView(binding.root)

        // 배경 투명하게 바꿔줌
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        initClickListener()
    }

    private fun initClickListener(){
        binding.apply {
            btnUp.setOnClickListener {
                val currentValue = tvGoalAmount.text.toString().toInt()
                if(currentType == GOAL_TYPE_TIME && currentValue < 600){
                    tvGoalAmount.text = (currentValue + 10).toString()
                }else if(currentType == GOAL_TYPE_DISTANCE && currentValue < 60){
                    tvGoalAmount.text = (currentValue + 1).toString()
                }
            }
            btnDown.setOnClickListener {
                val currentValue = tvGoalAmount.text.toString().toInt()
                if(currentType == GOAL_TYPE_TIME && currentValue > 10){
                    tvGoalAmount.text = (currentValue- 10).toString()
                }else if(currentType == GOAL_TYPE_DISTANCE && currentValue > 1){
                    tvGoalAmount.text = (currentValue - 1).toString()
                }
            }
            radioGroupPurpose.setOnCheckedChangeListener { _, checkId ->
                when (checkId) {
                    R.id.radio_btn_time -> {
                        tvGoalAmount.text = "30"
                        tvGoalType.text = "분"
                        currentType = GOAL_TYPE_TIME
                    }
                    R.id.radio_btn_distance -> {
                        tvGoalAmount.text = "3"
                        tvGoalType.text = "km"
                        currentType = GOAL_TYPE_DISTANCE
                    }
                }
            }
            btnOk.setOnClickListener {
                var currentValue = tvGoalAmount.text.toString().toInt()
                if(currentType == GOAL_TYPE_TIME){
                    currentValue *= 60
                }else{
                    currentValue *= 1000
                }
                listener.onItemClick(currentType, currentValue)
                dismiss()
            }
            btnCancel.setOnClickListener {
                dismiss()
            }
        }
    }
}