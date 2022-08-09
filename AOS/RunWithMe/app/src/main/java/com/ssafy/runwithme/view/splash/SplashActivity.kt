package com.ssafy.runwithme.view.splash

import android.content.Intent
import android.os.Handler
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseActivity
import com.ssafy.runwithme.databinding.ActivitySplashBinding
import com.ssafy.runwithme.view.login.LoginActivity

class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    override fun init() {
        val handler = Handler()
        handler.postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            this.finish() }
            ,3000) // 3초 이미지 띄우기
    }

    override fun onBackPressed() {} // 뒤로가기 버튼 비활성화
}