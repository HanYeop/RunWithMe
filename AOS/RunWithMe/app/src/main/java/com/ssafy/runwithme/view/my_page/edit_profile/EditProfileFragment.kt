package com.ssafy.runwithme.view.my_page.edit_profile

import android.app.Activity
import android.content.Intent
import android.text.InputFilter
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
import com.ssafy.runwithme.model.dto.UserDto
import com.ssafy.runwithme.view.my_page.MyPageViewModel
import java.util.regex.Pattern

class EditProfileFragment : BaseFragment<FragmentEditProfileBinding>(R.layout.fragment_edit_profile) {

    private val myPageViewModel by activityViewModels<MyPageViewModel>()

    override fun init() {

        binding.myPageVM = myPageViewModel

        initClickListener()
        initNickNameRule() // 닉네임 규칙 설정
        initSpinner() // 키와 몸무게 스피너 값 넣기
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
            btnModify.setOnClickListener {
                if(editJoinNickname.text.length < 2){ // 최소 길이는 따로 검증해줘야함
                    showToast("닉네임은 한글, 영문, 숫자로만 2자 ~ 8자까지 입력 가능합니다.")
                } else {
                    myPageViewModel.editMyProfile(
                        userDto = UserDto(
                            spinnerEditHeight.selectedItem as Int, spinnerEditWeight.selectedItem as Int,
                            editJoinNickname.text.toString(), "", -1
                        )
                    )
                }
            }
            // 프로필 이미지 변경 아이콘 클릭
            imageEditPhoto.setOnClickListener {
                pickPhotoGallery()
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
        }
    }

    private fun initSpinner(){
        val heightList = Array(131) { i -> i + 120 }
        val weightList = Array(231) { i -> i + 20 }

        binding.spinnerEditHeight.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, heightList)
        binding.spinnerEditHeight.setSelection(myPageViewModel.height.value.toInt()) // 초기 값 설정 - 원래 유저 값 가져오기
        binding.spinnerEditHeight.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                showToast((position + 120).toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }

        binding.spinnerEditWeight.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, weightList)
        binding.spinnerEditHeight.setSelection(myPageViewModel.weight.value.toInt()) // 초기 값 설정 - 원래 유저 값 가져오기
        binding.spinnerEditWeight.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                showToast((position + 20).toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }
    }

    private fun initNickNameRule(){
        binding.editJoinNickname.filters = arrayOf(
            InputFilter { src, start, end, dst, dstart, dend ->
                //val ps = Pattern.compile("^[a-zA-Z0-9ㄱ-ㅎ가-흐]+$") // 영문 숫자 한글
                //영문 숫자 한글 천지인 middle dot[ᆞ]
                val ps = Pattern.compile("^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ\\u318D\\u119E\\u11A2\\u2022\\u2025a\\u00B7\\uFE55]+$")
                if (src.equals("") || ps.matcher(src).matches()) {
                    return@InputFilter src;
                }
                showToast("닉네임은 한글, 영문, 숫자로만 2자 ~ 8자까지 입력 가능합니다.")
                return@InputFilter "";
            }
            , InputFilter.LengthFilter(8))
    }

}