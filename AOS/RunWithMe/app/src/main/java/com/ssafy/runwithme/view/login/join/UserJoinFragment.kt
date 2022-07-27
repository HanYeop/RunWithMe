package com.ssafy.runwithme.view.login.join

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.ssafy.runwithme.MainActivity
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentUserJoinBinding
import com.ssafy.runwithme.model.dto.UserDto
import com.ssafy.runwithme.utils.TAG
import com.ssafy.runwithme.view.login.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserJoinFragment : BaseFragment<FragmentUserJoinBinding>(R.layout.fragment_user_join) {

    private val userViewModel by activityViewModels<UserViewModel>()

    private val args by navArgs<UserJoinFragmentArgs>()

    override fun init() {
        binding.userVM = userViewModel

        initSpinner() // 키와 몸무게 스피너 값 넣기

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

    private fun initViewModelCallback(){
        userViewModel.loginEvent.observe(viewLifecycleOwner){
            showToast(it)
            startActivity(Intent(requireContext(), MainActivity::class.java))
            requireActivity().finish()
        }
        userViewModel.errorMsgEvent.observe(viewLifecycleOwner){
            showToast(it)
        }
        userViewModel.joinMsgEvent.observe(viewLifecycleOwner){
            showToast(it)
        }
    }

    private fun initClickListener(){
        binding.apply{
            btnJoin.setOnClickListener {
                // 닉네임 규칙검사

                userViewModel.joinUser(args.tmptoken,
                userDto = UserDto(spinnerHeight.selectedItem as Int, spinnerWeight.selectedItem as Int, 
                    editJoinNickname.text.toString())
                )
                Log.d(TAG, "initClickListener: ${spinnerHeight.selectedItem}")
            }
        }
    }

}