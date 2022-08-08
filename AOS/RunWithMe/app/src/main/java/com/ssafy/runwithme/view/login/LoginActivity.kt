package com.ssafy.runwithme.view.login

import android.content.Intent
import android.content.SharedPreferences
import com.ssafy.runwithme.MainActivity
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseActivity
import com.ssafy.runwithme.databinding.ActivityMainBinding
import com.ssafy.runwithme.utils.JWT
import com.ssafy.runwithme.utils.PERMISSION_OK
import com.ssafy.runwithme.view.permission.PermissionActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_login) {

    @Inject
    lateinit var sharedPref: SharedPreferences

    override fun init() {
        if(!sharedPref.getBoolean(PERMISSION_OK, false)){
            startActivity(Intent(this, PermissionActivity::class.java))
            finish()
        }

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