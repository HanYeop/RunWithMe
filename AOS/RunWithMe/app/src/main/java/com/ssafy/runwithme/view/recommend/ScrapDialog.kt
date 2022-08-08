package com.ssafy.runwithme.view.recommend

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import com.ssafy.runwithme.R
import com.ssafy.runwithme.databinding.DialogScrapBinding
import com.ssafy.runwithme.utils.dialogResize

class ScrapDialog(context: Context, private val listener : ScrapDialogListener, private val isAdd : Boolean): Dialog(context) {

    private lateinit var binding: DialogScrapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_scrap,
            null,
            false
        )
        setContentView(binding.root)

        initClickListener()
    }

    override fun onStart() {
        super.onStart()

        initView()
    }

    private fun initView(){
        context.dialogResize(this, 0.9f, 0.3f)

        // 배경 투명하게 바꿔줌
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        if(isAdd){
            binding.tvDialogHeader.text = "러닝 코스 스크랩 추가"
            binding.etScrapTitle.visibility = View.VISIBLE
            binding.tvScrapDelete.visibility = View.GONE
            binding.btnOk.text = "추가"
        } else {
            binding.tvDialogHeader.text = "스크랩 삭제"
            binding.tvScrapDelete.visibility = View.VISIBLE
            binding.etScrapTitle.visibility = View.GONE
            binding.btnOk.text = "삭제"
        }
    }

    private fun initClickListener() {
        binding.apply {
            btnOk.setOnClickListener {
                if(isAdd){
                    var title = etScrapTitle.text.toString()
                    listener.onItemAdd(title)
                } else {
                    listener.onItemDelete()
                }
                dismiss()
            }
            btnCancel.setOnClickListener { dismiss() }
        }
    }
}