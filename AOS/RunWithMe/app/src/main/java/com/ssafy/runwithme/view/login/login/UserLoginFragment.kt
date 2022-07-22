package com.ssafy.runwithme.view.login.login

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
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

        initClickListener()
    }

    private fun initClickListener(){
        binding.apply {
            imgBtnLoginGoogle.setOnClickListener {
                googleSignIn()
            }
        }
    }

    private fun googleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestServerAuthCode(resources.getString(R.string.google_client_key))
            .requestEmail()
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
                val accessToken = account.serverAuthCode!!
                showToast(accessToken)
                Log.d("test5", ":$accessToken ")
//                viewModel.socialLogin(GOOGLE, accessToken)
            } catch (e: ApiException) {
                Log.w("test5", "signInResult:failed code=" + e.statusCode)
            }
        } else {
            Log.d("test5", "$it: ")
        }
    }
}