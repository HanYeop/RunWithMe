package com.ssafy.runwithme.view.my_page.edit_profile

import androidx.navigation.fragment.findNavController
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentEditProfileBinding

class EditProfileFragment : BaseFragment<FragmentEditProfileBinding>(R.layout.fragment_edit_profile) {
    override fun init() {
        initClickListener()
    }

    private fun initClickListener() {
        binding.apply {
            btnModify.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
}