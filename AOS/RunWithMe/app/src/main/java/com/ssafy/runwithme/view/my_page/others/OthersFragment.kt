package com.ssafy.runwithme.view.my_page.others

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentOthersBinding
import com.ssafy.runwithme.utils.JWT
import com.ssafy.runwithme.view.login.LoginActivity
import com.ssafy.runwithme.view.my_page.MyPageViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OthersFragment : BaseFragment<FragmentOthersBinding>(R.layout.fragment_others) {

    @Inject
    lateinit var sharedPreferences: SharedPreferences
    private val myPageViewModel by viewModels<MyPageViewModel>()

    override fun init() {
        initClickListener()

        initViewModelCallBack()
    }

    private fun initClickListener(){
        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            tvQuestion.setOnClickListener {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse("http://pf.kakao.com/_xbxnlqxj")
                startActivity(i)
            }
            tvTerms.setOnClickListener {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse("https://hanyeop.github.io/RunWithMe-terms/")
                startActivity(i)
            }
            // 로그아웃 시 저장된 JWT 토큰, FCM 토큰을 삭제한다.
            tvLogout.setOnClickListener {
                logout()
            }
        }
    }

    private fun logout(){
        myPageViewModel.deleteFcmToken()
    }

    private fun initViewModelCallBack(){
        myPageViewModel.logoutEvent.observe(viewLifecycleOwner){
            sharedPreferences.edit().putString(JWT,"").apply()
            requireActivity().finish()
            requireActivity().startActivity(Intent(requireContext(),LoginActivity::class.java))
        }
    }
}