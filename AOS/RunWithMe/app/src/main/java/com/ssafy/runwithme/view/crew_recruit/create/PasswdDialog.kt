package com.ssafy.runwithme.view.crew_recruit.create

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.ssafy.runwithme.R
import com.ssafy.runwithme.databinding.DialogPasswdBinding
import com.ssafy.runwithme.utils.dialogResize

class PasswdDialog(context: Context, private val listener : PasswdDialogListener): Dialog(context) {

    private lateinit var binding: DialogPasswdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_passwd,
            null,
            false
        )
        setContentView(binding.root)

//        context.dialogResize(this, 0.9f, 0.5f)

        // 배경 투명하게 바꿔줌
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        initClickListener()
    }

    private fun initClickListener() {
        binding.apply {
            tvPositive.setOnClickListener {
                val passwd = etPasswd.text.toString()
                listener.onItemClick(passwd)
                dismiss()
            }
        }
    }

}