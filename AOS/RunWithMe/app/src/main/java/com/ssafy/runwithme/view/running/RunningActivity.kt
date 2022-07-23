package com.ssafy.runwithme.view.running

import android.app.AlertDialog
import android.content.Intent
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseActivity
import com.ssafy.runwithme.databinding.ActivityRunningBinding
import com.ssafy.runwithme.service.RunningService
import com.ssafy.runwithme.utils.ACTION_STOP_SERVICE
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RunningActivity : BaseActivity<ActivityRunningBinding>(R.layout.activity_running) {
    override fun init() {

    }

    // 뒤로가기 버튼 눌렀을 때
    override fun onBackPressed() {
        var builder = AlertDialog.Builder(this)
        builder.setTitle("달리기를 취소할까요? 기록은 저장되지 않습니다.")
            .setPositiveButton("네"){ _,_ ->
                // TODO : 달리기 종료시킴
                stopRun()
            }
            .setNegativeButton("아니오"){_,_ ->
                // 다시 시작
            }.create()
        builder.show()
    }

    // 달리기 종료
    private fun stopRun() {
        sendCommandToService(ACTION_STOP_SERVICE)
        finish()
    }

    // 서비스에게 명령을 전달함
    private fun sendCommandToService(action : String) =
        Intent(this,RunningService::class.java).also {
            it.action = action
            this.startService(it)
        }
}