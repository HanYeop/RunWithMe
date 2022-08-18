package com.ssafy.runwithme.view.login.join

import android.content.Intent
import android.text.InputFilter
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.google.firebase.messaging.FirebaseMessaging
import com.ssafy.runwithme.MainActivity
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentUserJoinBinding
import com.ssafy.runwithme.model.dto.FcmTokenDto
import com.ssafy.runwithme.model.dto.UserDto
import com.ssafy.runwithme.utils.TAG
import com.ssafy.runwithme.view.login.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern

@AndroidEntryPoint
class UserJoinFragment : BaseFragment<FragmentUserJoinBinding>(R.layout.fragment_user_join) {

    private val userViewModel by activityViewModels<UserViewModel>()

    private val args by navArgs<UserJoinFragmentArgs>()

    override fun init() {
        binding.userVM = userViewModel

        initSpinner() // 키와 몸무게 스피너 값 넣기

        initNickNameRule()

        initViewModelCallback()

        initClickListener()
    }

    private fun initSpinner(){
        val heightList = Array(131) { i -> i + 120 }
        val weightList = Array(231) { i -> i + 20 }

        binding.spinnerHeight.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, heightList)
        binding.spinnerHeight.setSelection(30) // 초기 값 설정
        binding.spinnerHeight.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {}
            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }

        binding.spinnerWeight.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, weightList)
        binding.spinnerWeight.setSelection(30) // 초기 값 설정
        binding.spinnerWeight.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {}
            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }
    }

    private fun initNickNameRule(){
        binding.editJoinNickname.filters = arrayOf(
            InputFilter { src, start, end, dst, dstart, dend ->
                // val ps = Pattern.compile("^[a-zA-Z0-9ㄱ-ㅎ가-흐]+$") // 영문 숫자 한글
                // 영문 숫자 한글 천지인 middle dot[ᆞ]
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

    private fun initViewModelCallback(){
        userViewModel.loginEvent.observe(viewLifecycleOwner){
            showToast(it)
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if(!task.isSuccessful){
                    return@addOnCompleteListener
                }else{
                    userViewModel.fcmToken(FcmTokenDto(task.result))
                }
            }
        }
        userViewModel.fcmEvent.observe(viewLifecycleOwner){
            startActivity(Intent(requireContext(), MainActivity::class.java))
            requireActivity().finish()
        }
        userViewModel.errorMsgEvent.observe(viewLifecycleOwner){
            showToast(it)
        }
        userViewModel.joinMsgEvent.observe(viewLifecycleOwner){
            showToast(it)
        }
        userViewModel.failMsgEvent.observe(viewLifecycleOwner){
            showToast(it)
        }
    }

    private fun initClickListener() {
        binding.apply {
            btnJoin.setOnClickListener {
                if(editJoinNickname.text.length < 2){ // 최소 길이는 따로 검증해줘야함
                    showToast("닉네임은 한글, 영문, 숫자로만 2자 ~ 8자까지 입력 가능합니다.")
                } else {
                    userViewModel.joinUser(
                        args.tmptoken,
                        userDto = UserDto(
                            spinnerHeight.selectedItem as Int, spinnerWeight.selectedItem as Int,
                            editJoinNickname.text.toString(), "", -1, null)
                    )
                }
            }
        }
    }

}