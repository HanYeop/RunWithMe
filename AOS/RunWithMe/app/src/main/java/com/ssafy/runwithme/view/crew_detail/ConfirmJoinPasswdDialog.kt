package com.ssafy.runwithme.view.crew_detail

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.NumberPicker
import androidx.databinding.DataBindingUtil
import com.ssafy.runwithme.R
import com.ssafy.runwithme.databinding.DialogConfirmJoinBinding
import com.ssafy.runwithme.databinding.DialogConfirmJoinPasswdBinding
import com.ssafy.runwithme.databinding.DialogWeeksBinding
import com.ssafy.runwithme.view.crew_recruit.create.GoalWeeksDialogListener

class ConfirmJoinPasswdDialog (context: Context, private val text : String, private val listener : ConfirmJoinPasswdListener): Dialog(context) {

    private lateinit var binding: DialogConfirmJoinPasswdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_confirm_join_passwd,
            null,
            false
        )
        setContentView(binding.root)


        binding.apply {
            tvConfirmText.text = text
        }

//        context.dialogResize(this, 0.8f, 0.5f)

        // 배경 투명하게 바꿔줌
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        initClickListener()
    }

    private fun initClickListener() {
        binding.apply {
            btnConfirm.setOnClickListener {
                listener.onItemClick(editPasswd.text.toString())
                dismiss()
            }

            btnCancel.setOnClickListener {
                dismiss()
            }
        }
    }
}