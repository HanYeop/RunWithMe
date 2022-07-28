package com.ssafy.runwithme.view.my_page.edit_profile

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.text.InputFilter
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentEditProfileBinding
import com.ssafy.runwithme.model.dto.ProfileEditDto
import com.ssafy.runwithme.utils.TAG
import com.ssafy.runwithme.view.my_page.MyPageViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.regex.Pattern

class EditProfileFragment : BaseFragment<FragmentEditProfileBinding>(R.layout.fragment_edit_profile) {

    private val myPageViewModel by activityViewModels<MyPageViewModel>()
    private var imgFile : MultipartBody.Part? = null

    override fun init() {
        binding.myPageVM = myPageViewModel

        initSpinner() // 키와 몸무게 스피너 값 넣기

        initNickNameRule() // 닉네임 규칙 설정

        initClickListener()

        initViewModelCallback()
    }

    private fun initViewModelCallback(){
        myPageViewModel.editMsgEvent.observe(viewLifecycleOwner){
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
            imageEditPhoto.setOnClickListener {
                pickPhotoGallery()
            }
            // 수정하기
            btnModify.setOnClickListener {
                if(editJoinNickname.text.toString().length < 2){ // 최소 길이는 따로 검증해줘야함
                    showToast("닉네임은 한글, 영문, 숫자로만 2자 ~ 8자까지 입력 가능합니다.")
                } else {
                    myPageViewModel.editMyProfile(
                        profile = ProfileEditDto(
                            editJoinNickname.text.toString(),
                            spinnerEditHeight.selectedItem as Int,
                            spinnerEditWeight.selectedItem as Int,
                        ),
                        imgFile
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
            binding.imageUserProfile.setImageURI(it.data?.data)

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
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
        fileOutputStream.close()
        return newFile
    }

    private fun initSpinner(){
        val heightList = Array(131) { i -> i + 120 }
        val weightList = Array(231) { i -> i + 20 }

        binding.spinnerEditHeight.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, heightList)
        binding.spinnerEditHeight.setSelection(myPageViewModel.height.value.toInt() - 120) // 초기 값 설정 - 원래 유저 값 가져오기
        binding.spinnerEditHeight.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {}
            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }

        binding.spinnerEditWeight.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, weightList)
        binding.spinnerEditHeight.setSelection(myPageViewModel.weight.value.toInt() - 20) // 초기 값 설정 - 원래 유저 값 가져오기
        binding.spinnerEditWeight.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {}
            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }
    }

    private fun initNickNameRule(){
        binding.editJoinNickname.filters = arrayOf(
            InputFilter { src, _, _, _, _, _ ->
                //영문 숫자 한글 천지인 middle dot[ᆞ]
                val ps = Pattern.compile("^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ\\u318D\\u119E\\u11A2\\u2022\\u2025a\\u00B7\\uFE55]+$")
                if (src.equals("") || ps.matcher(src).matches()) {
                    return@InputFilter src;
                }
                showToast("닉네임은 한글, 영문, 숫자로만 2자 ~ 8자까지 입력 가능합니다.")
                return@InputFilter "";
            },
            InputFilter.LengthFilter(8)
        )
    }

}