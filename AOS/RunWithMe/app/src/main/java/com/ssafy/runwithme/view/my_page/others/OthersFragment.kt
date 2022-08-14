package com.ssafy.runwithme.view.my_page.others

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.ssafy.runwithme.BuildConfig
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
        binding.apply {
            tvVersionContent.text = BuildConfig.VERSION_NAME
        }
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
        startNaverDeleteToken()

        myPageViewModel.deleteFcmToken()
    }

    private fun startNaverDeleteToken(){
        NidOAuthLogin().callDeleteTokenApi(requireContext(), object : OAuthLoginCallback {
            override fun onSuccess() {
                //서버에서 토큰 삭제에 성공한 상태입니다.
//                Toast.makeText(requireContext(), "네이버 아이디 토큰삭제 성공!", Toast.LENGTH_SHORT).show()
            }
            override fun onFailure(httpStatus: Int, message: String) {
                // 서버에서 토큰 삭제에 실패했어도 클라이언트에 있는 토큰은 삭제되어 로그아웃된 상태입니다.
                // 클라이언트에 토큰 정보가 없기 때문에 추가로 처리할 수 있는 작업은 없습니다.
                Log.d("naver", "errorCode: ${NaverIdLoginSDK.getLastErrorCode().code}")
                Log.d("naver", "errorDesc: ${NaverIdLoginSDK.getLastErrorDescription()}")
            }
            override fun onError(errorCode: Int, message: String) {
                // 서버에서 토큰 삭제에 실패했어도 클라이언트에 있는 토큰은 삭제되어 로그아웃된 상태입니다.
                // 클라이언트에 토큰 정보가 없기 때문에 추가로 처리할 수 있는 작업은 없습니다.
                onFailure(errorCode, message)
            }
        })
    }

    private fun initViewModelCallBack(){
        myPageViewModel.logoutEvent.observe(viewLifecycleOwner){
            sharedPreferences.edit().putString(JWT,"").apply()
            requireActivity().finish()
            requireActivity().startActivity(Intent(requireContext(),LoginActivity::class.java))
        }
    }
}