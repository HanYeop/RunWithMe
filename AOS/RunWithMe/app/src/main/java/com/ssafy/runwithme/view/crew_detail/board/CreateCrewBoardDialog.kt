package com.ssafy.runwithme.view.crew_detail.board

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.ssafy.runwithme.R
import com.ssafy.runwithme.databinding.DialogCreateBoardBinding
import com.ssafy.runwithme.databinding.DialogWeeksBinding
import com.ssafy.runwithme.utils.TAG
import com.ssafy.runwithme.utils.dialogResize
import com.ssafy.runwithme.view.crew_recruit.create.GoalWeeksDialogListener

class CreateCrewBoardDialog(context: Context, private val listener : CreateCrewBoardListener): Dialog(context) {

    private lateinit var binding: DialogCreateBoardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_create_board,
            null,
            false
        )
        setContentView(binding.root)


        context.dialogResize(this, 0.9f, 0.6f)

        // 배경 투명하게 바꿔줌
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        initClickListener()
    }

    private fun initClickListener() {
        binding.apply {
            btnRecommend.setOnClickListener {
                val content = etBoardContent.text.toString()
                Log.d(TAG, "initClickListener: content: $content")
                listener.onItemClick(content)
                dismiss()
            }
        }
    }
}