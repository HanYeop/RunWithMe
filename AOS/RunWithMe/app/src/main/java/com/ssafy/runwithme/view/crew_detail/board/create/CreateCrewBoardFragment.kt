package com.ssafy.runwithme.view.crew_detail.board.create

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.gson.Gson
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentCreateCrewBoardBinding
import com.ssafy.runwithme.model.dto.CreateCrewBoardDto
import com.ssafy.runwithme.utils.resizeBitmapFormUri
import com.ssafy.runwithme.view.crew_detail.board.CrewBoardViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@AndroidEntryPoint
class CreateCrewBoardFragment : BaseFragment<FragmentCreateCrewBoardBinding>(R.layout.fragment_create_crew_board) {

    private val crewBoardViewModel by viewModels<CrewBoardViewModel>()

    private val args by navArgs<CreateCrewBoardFragmentArgs>()
    private var crewSeq = 0
    private var imgFile : MultipartBody.Part? = null

    override fun init() {
        crewSeq = args.crewid

        initClickListener()

        initViewModelCallback()
    }

    private fun initViewModelCallback(){
        crewBoardViewModel.errorMsgEvent.observe(viewLifecycleOwner){
            showToast(it)
        }

        crewBoardViewModel.failMsgEvent.observe(viewLifecycleOwner){
            showToast(it)
        }

        crewBoardViewModel.successMsgEvent.observe(viewLifecycleOwner){
            showToast(it)
            findNavController().popBackStack()
        }
    }

    private fun initClickListener() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            // 프로필 이미지 변경 아이콘 클릭
            imageRecommend.setOnClickListener {
                pickPhotoGallery()

                binding.imageRecommendPhoto.visibility = View.GONE
            }
            btnCancel.setOnClickListener {
                findNavController().popBackStack()
            }
            btnRecommend.setOnClickListener {
                if(etBoardContent.text.isEmpty()){ // 내용이 비었을 시
                    showToast("게시할 내용을 입력 해주세요.")
                }
                else {
                    val crewBoard = CreateCrewBoardDto(0, etBoardContent.text.toString())
                    val json = Gson().toJson(crewBoard)
                    val crewBoardDto = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), json)

                    crewBoardViewModel.createCrewBoard(crewSeq, crewBoardDto, imgFile)
                }
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
            binding.imageRecommend.setImageURI(it.data?.data)

            var bitmap : Bitmap?
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
        else if(it.resultCode == Activity.RESULT_CANCELED){ // 아무 사진도 선택하지 않은 경우
            binding.imageRecommendPhoto.visibility = View.VISIBLE
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
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, fileOutputStream)
        fileOutputStream.close()
        return newFile
    }

}