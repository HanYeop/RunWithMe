package com.ssafy.runwithme.view.create_recommend

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.ssafy.runwithme.R
import com.ssafy.runwithme.databinding.DialogCreateRecommendBinding

class CreateRecommendDialog(context: Context, private val listener: CreateRecommendListener): Dialog(context) {

    private lateinit var binding: DialogCreateRecommendBinding

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_create_recommend, null, false)
        setContentView(binding.root)

        // 배경 투명하게 바꿔줌
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // 전체화면
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        initClickListener()
    }

    private fun initClickListener(){
        binding.apply {
            // 프로필 이미지 변경 아이콘 클릭
            imageRecommendPhoto.setOnClickListener {
                pickPhotoGallery()
            }

            btnCancel.setOnClickListener {
                dismiss()
            }

            btnRecommend.setOnClickListener {
                listener.onBtnOkClicked(binding.ratingEnvironment.rating.toInt(), binding.ratingHard.rating.toInt())
                dismiss()
            }
        }
    }

    private fun pickPhotoGallery() {
        val photoIntent = Intent(Intent.ACTION_PICK)
        photoIntent.type = "image/*"
        pickPhotoResult.launch(photoIntent)
    }

    private val pickPhotoResult : ActivityResultLauncher<Intent> = ownerActivity.register registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == Activity.RESULT_OK){
            binding.imageRecommendPhoto.setImageURI(it.data?.data)

            var bitmap : Bitmap?
            val uri = it.data?.data
            try {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                    bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireActivity().contentResolver, uri!!))
                }else{
                    bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)
                }
                createMultiPart(bitmap!!)
            } catch (e : Exception){
                e.printStackTrace()
            }
        }
    }
}