package com.ssafy.runwithme.view.permission

import android.content.Intent
import android.content.SharedPreferences
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseActivity
import com.ssafy.runwithme.databinding.ActivityPermissionBinding
import com.ssafy.runwithme.utils.PERMISSION_OK
import com.ssafy.runwithme.view.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PermissionActivity : BaseActivity<ActivityPermissionBinding>(R.layout.activity_permission) {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun init() {
        binding.apply {
            btnOk.setOnClickListener {
                sharedPreferences.edit().putBoolean(PERMISSION_OK,true).apply()
                startActivity(Intent(this@PermissionActivity,LoginActivity::class.java))
                finish()
            }
        }
    }
}