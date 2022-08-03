package com.ssafy.runwithme.view.login

import android.content.Intent
import android.content.SharedPreferences
import com.ssafy.runwithme.MainActivity
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseActivity
import com.ssafy.runwithme.databinding.ActivityMainBinding
import com.ssafy.runwithme.utils.JWT
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_login) {

    @Inject
    lateinit var sharedPref: SharedPreferences

    override fun init() {
        autoLogin()
    }

    private fun autoLogin(){
        val jwt = sharedPref.getString(JWT, "")

        if(jwt != ""){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}