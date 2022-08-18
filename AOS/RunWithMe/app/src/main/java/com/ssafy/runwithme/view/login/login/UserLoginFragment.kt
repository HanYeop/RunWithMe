package com.ssafy.runwithme.view.login.login

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import com.ssafy.runwithme.MainActivity
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentUserLoginBinding
import com.ssafy.runwithme.model.dto.FcmTokenDto
import com.ssafy.runwithme.view.login.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserLoginFragment : BaseFragment<FragmentUserLoginBinding>(R.layout.fragment_user_login) {

    private val userViewModel by activityViewModels<UserViewModel>()

    override fun init() {
        binding.userVm = userViewModel

        initClickListener()

        initViewModelCallBack()
    }

    private fun initClickListener(){
        binding.apply {
            // TEST
            imgLogo.setOnClickListener {
                startActivity(Intent(requireContext(),MainActivity::class.java))
                requireActivity().finish()
            }
            cardLoginGoogle.setOnClickListener {
                googleSignIn()
            }
            cardLoginNaver.setOnClickListener {
                naverSignIn()
            }
            cardLoginKakao.setOnClickListener {
                kakaoSignIn()
            }
        }
    }

    private fun initViewModelCallBack(){
        userViewModel.joinEvent.observe(viewLifecycleOwner) {
            val action = UserLoginFragmentDirections.actionUserLoginFragmentToUserJoinFragment(it)
            findNavController().navigate(action)
        }
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
    }

    private fun googleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Scope(Scopes.EMAIL))
            .requestServerAuthCode(resources.getString(R.string.google_client_key))
            .requestEmail()
            .requestIdToken(getString(R.string.google_client_key))
            .build()

        val mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        googleSignInResult.launch(signInIntent)
    }

    private val googleSignInResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {

        if (it.resultCode == Activity.RESULT_OK) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val accessToken = account.idToken!!

                userViewModel.googleLogin(accessToken)
            } catch (e: ApiException) {
                Log.w("test5", "signInResult:failed code=" + e.statusCode)
            }
        } else {
            Log.d("test5", "$it: ")
        }
    }

    val TAG = "test5"
    private fun kakaoSignIn(){
        Log.e(TAG, "카카오1")
        // 카카오계정으로 로그인 공통 callback 구성
        // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            Log.e(TAG, "카카오8")
            if (error != null) {
                Log.e(TAG, "카카오2")
                Log.e(TAG, "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Log.e(TAG, "카카오3")
                Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")

                userViewModel.kakaoLogin(token.accessToken)

                // 사용자 정보 요청 (기본)
                UserApiClient.instance.me { user, error ->
                    if (error != null) {
                        Log.e(TAG, "카카오4")
                        Log.e(TAG, "사용자 정보 요청 실패", error)
                    }
                    else if (user != null) {
                        Log.e(TAG, "카카오5")
                        Log.i(TAG, "사용자 정보 요청 성공" +
                                "\n회원번호: ${user.id}" +
                                "\n이메일: ${user.kakaoAccount?.email}" +
                                "\n닉네임: ${user.kakaoAccount?.profile?.nickname}")
                    }else{
                        Log.e(TAG, "카카오6")
                    }
                }
            }else{
                Log.e(TAG, "카카오7")
            }
        }

        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {
            UserApiClient.instance.loginWithKakaoTalk(requireContext()) { token, error ->
                if (error != null) {
                    Log.e(TAG, "카카오톡으로 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }
                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
                } else if (token != null) {
                    UserApiClient.instance.me { user, error ->
                        if (error != null) {
                            Log.e(TAG, "사용자 정보 요청 실패", error)
                        }
                        else if (user != null) {
                            Log.i(TAG, "사용자 정보 요청 성공" +
                                    "\n회원번호: ${user.id}" +
                                    "\n이메일: ${user.kakaoAccount?.email}" +
                                    "\n닉네임: ${user.kakaoAccount?.profile?.nickname}")
                        }
                    }
                    Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")

                    userViewModel.kakaoLogin(token.accessToken)
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
        }
    }


    private fun naverSignIn(){
        val oauthLoginCallback = object : OAuthLoginCallback {
            override fun onSuccess() {
                NidOAuthLogin().callProfileApi(object : NidProfileCallback<NidProfileResponse> {
                    override fun onSuccess(result: NidProfileResponse) {
                        val email = result.profile?.email.toString()
                        Log.d(TAG, "onSuccess: ${result.profile}")
                        Log.d(TAG, "onSuccess: $email")

                        Log.d(TAG, "onSuccess: ${NaverIdLoginSDK.getAccessToken()!!}")
                        userViewModel.naverLogin(NaverIdLoginSDK.getAccessToken()!!, email)
                    }
                    override fun onError(errorCode: Int, message: String) {}
                    override fun onFailure(httpStatus: Int, message: String) {}
                })
            }

            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                showToast("errorCode:$errorCode, errorDesc:$errorDescription")
            }

            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }

        NaverIdLoginSDK.authenticate(requireContext(), oauthLoginCallback)
    }
}