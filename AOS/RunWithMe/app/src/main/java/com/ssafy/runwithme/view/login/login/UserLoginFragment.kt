package com.ssafy.runwithme.view.login.login

import androidx.fragment.app.viewModels
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentUserLoginBinding
import com.ssafy.runwithme.view.login.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserLoginFragment : BaseFragment<FragmentUserLoginBinding>(R.layout.fragment_user_login) {

    private val userViewModel by viewModels<UserViewModel>()

    override fun init() {
        binding.userVm = userViewModel
    }
}