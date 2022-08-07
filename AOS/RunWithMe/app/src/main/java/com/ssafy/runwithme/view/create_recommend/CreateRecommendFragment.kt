package com.ssafy.runwithme.view.create_recommend

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentCreateRecommendBinding
import com.ssafy.runwithme.view.recommend.RecommendViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@AndroidEntryPoint
class CreateRecommendFragment : BaseFragment<FragmentCreateRecommendBinding>(R.layout.fragment_create_recommend) {

    private val recommendViewModel by activityViewModels<RecommendViewModel>()
    private val args by navArgs<CreateRecommendFragmentArgs>()

    private var imgFile : MultipartBody.Part? = null
    private var runRecordSeq = 0

    override fun init() {
        runRecordSeq = args.runrecordseq

        initClickListener()

        initViewModelCallBack()
    }

    private fun initViewModelCallBack(){
        recommendViewModel.successMsgEvent.observe(viewLifecycleOwner){
            showToast(it)
            findNavController().popBackStack()
        }

        recommendViewModel.errorMsgEvent.observe(viewLifecycleOwner){
            showToast(it)
        }
    }

    private fun initClickListener(){
        binding.apply {
            // 프로필 이미지 변경 아이콘 클릭
            imageRecommend.setOnClickListener {
                pickPhotoGallery()

                binding.imageRecommendPhoto.visibility = View.GONE
                binding.tvImageText.visibility = View.GONE
            }

            btnCancel.setOnClickListener {
                findNavController().popBackStack()
            }

            btnRecommend.setOnClickListener {
                if(imgFile == null){ // 이미지를 선택 안 할시
                    showToast("러닝 코스 사진을 업로드 해주세요.")
                }
                else if(binding.etRecommendContent.text.isEmpty()){ // 내용이 비었을 시
                    showToast("추천 사유를 입력 해주세요.")
                }
                else {
                    recommendViewModel.createRecommend(
                        binding.ratingEnvironment.rating.toInt(), binding.ratingHard.rating.toInt(), runRecordSeq,
                        binding.etRecommendContent.text.toString(), imgFile!!
                    )
                }
            }
        }
    }

    private fun pickPhotoGallery() {
        val photoIntent = Intent(Intent.ACTION_PICK)
        photoIntent.type = "image/*"
        pickPhotoResult.launch(photoIntent)
    }

    private val pickPhotoResult : ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == Activity.RESULT_OK){
            binding.imageRecommend.setImageURI(it.data?.data)

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
        else if(it.resultCode == Activity.RESULT_CANCELED){ // 아무 사진도 선택하지 않은 경우
            binding.imageRecommendPhoto.visibility = View.VISIBLE
            binding.tvImageText.visibility = View.VISIBLE
        }
    }

    private fun createMultiPart(bitmap: Bitmap) {
        var imageFile: File? = null
        try {
            imageFile = createFileFromBitmap(bitmap)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), imageFile!!)
        imgFile = MultipartBody.Part.createFormData("imgFile", imageFile!!.name, requestFile)
    }

    @Throws(IOException::class)
    private fun createFileFromBitmap(bitmap: Bitmap): File? {
        val newFile = File(requireActivity().filesDir, "profile_${System.currentTimeMillis()}")
        val fileOutputStream = FileOutputStream(newFile)
        bitmap.compress(Bitmap.CompressFormat.PNG, 40, fileOutputStream)
        fileOutputStream.close()
        return newFile
    }
}