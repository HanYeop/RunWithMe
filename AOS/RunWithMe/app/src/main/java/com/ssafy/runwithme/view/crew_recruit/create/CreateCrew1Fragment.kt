package com.ssafy.runwithme.view.crew_recruit.create

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentCreateCrew1Binding
import com.ssafy.runwithme.utils.resizeBitmapFormUri
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class CreateCrewFragment1 : BaseFragment<FragmentCreateCrew1Binding>(R.layout.fragment_create_crew1) {

    private val createCrewViewModel by activityViewModels<CreateCrewViewModel>()
    private var imgFile : MultipartBody.Part? = null

    override fun init() {
        binding.apply {
            crewCreateVM = createCrewViewModel
        }

        initClickListener()
    }

    @SuppressLint("ResourceAsColor")
    private fun initClickListener() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            btnCreateNext.setOnClickListener {
                if(editCreateCrewName.text.toString().length < 2){
                    showToast("크루 이름은 2글자 이상이여야 합니다.")
                }else if(editCreateCrewDescription.text.toString() == ""){
                    showToast("크루 소개를 입력해주세요.")
                }else {
                    createCrewViewModel.setImgFile(imgFile)
                    findNavController().navigate(R.id.action_createCrewFragment1_to_createCrewFragment2)
                }
            }

            imgCreateCrew.setOnClickListener {
                pickPhotoGallery()
            }

            layoutImg.setOnClickListener {
                pickPhotoGallery()
            }
        }
    }



    private fun pickPhotoGallery() {
        val photoIntent = Intent(Intent.ACTION_PICK)
        photoIntent.type = "image/*"
        photoIntent.putExtra("crop","true")
        pickPhotoResult.launch(photoIntent)
    }

    private val pickPhotoResult : ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == Activity.RESULT_OK){
            binding.imgCreateCrew.setImageURI(it.data?.data)
            binding.imgCreateCrew.alpha = 1.0F
            binding.apply {
                tvImageText.visibility = View.GONE
                tvImageText2.visibility = View.GONE
            }
            var bitmap : Bitmap? = null
            val uri = it.data?.data
            try {
                if(uri != null){
                    bitmap = resizeBitmapFormUri(uri,requireContext())
                    createMultiPart(bitmap!!)
                }
            } catch (e : Exception){
                e.printStackTrace()
            }
        }
    }

    @Throws(IOException::class)
    private fun createFileFromBitmap(bitmap: Bitmap): File {
        val newFile = File(requireActivity().filesDir, "crew_${System.currentTimeMillis()}")
        val fileOutputStream = FileOutputStream(newFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, fileOutputStream)
        fileOutputStream.close()
        return newFile
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

}