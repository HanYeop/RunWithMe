package com.ssafy.runwithme.view.crew_detail.board

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.ssafy.runwithme.R
import com.ssafy.runwithme.databinding.DialogReportBoardBinding
import com.ssafy.runwithme.utils.dialogResize

class ReportCrewBoardDialog(context:Context, private val boardSeq : Int, private val listener : ReportBoardListener) : Dialog(context) {

    private lateinit var binding : DialogReportBoardBinding
    lateinit var content : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_report_board,
            null, false
        )
        setContentView(binding.root)

        context.dialogResize(this, 0.9f, 0.6f)

        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        initClickListener()
    }

    private fun initClickListener(){
        binding.apply {
            btnReport.setOnClickListener {
                content = etBoardContent.text.toString()
                if(content.isEmpty()){
                    Toast.makeText(context, "내용을 입력하셔야 합니다.", Toast.LENGTH_SHORT).show()
                } else {
                    listener.onItemClick(content, boardSeq)
                    Toast.makeText(context, "신고가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    dismiss()
                }
            }
            btnCancel.setOnClickListener { dismiss() }
        }
    }
}