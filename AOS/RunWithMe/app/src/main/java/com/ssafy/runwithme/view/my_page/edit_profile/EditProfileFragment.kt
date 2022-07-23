package com.ssafy.runwithme.view.my_page.edit_profile

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentEditProfileBinding

class EditProfileFragment : BaseFragment<FragmentEditProfileBinding>(R.layout.fragment_edit_profile) {
    override fun init() {
        initClickListener()
        initSpinner() // 키와 몸무게 스피너 값 넣기
    }

    private fun initClickListener() {
        binding.apply {
            btnModify.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun initSpinner(){
        val heightList = Array(131) { i -> i + 120 }
        val weightList = Array(231) { i -> i + 20 }

        binding.spinnerEditHeight.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, heightList)
        binding.spinnerEditHeight.setSelection(30) // 초기 값 설정 - 원래 유저 값 가져오기
        binding.spinnerEditHeight.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                showToast((position + 120).toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }

        binding.spinnerEditWeight.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, weightList)
        binding.spinnerEditHeight.setSelection(30) // 초기 값 설정 - 원래 유저 값 가져오기
        binding.spinnerEditWeight.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                showToast((position + 20).toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }
    }

}