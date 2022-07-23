package com.ssafy.runwithme.view.login

import android.content.Intent
import com.ssafy.runwithme.MainActivity
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseActivity
import com.ssafy.runwithme.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_login) {

    override fun init() {

        // TEST
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }
}