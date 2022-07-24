package com.ssafy.runwithme.view.running.result

import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseActivity
import com.ssafy.runwithme.databinding.ActivityRunningResultBinding
import com.ssafy.runwithme.view.create_recommend.CreateRecommendDialog
import com.ssafy.runwithme.view.running.result.achievement.AchievementDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RunningResultActivity : BaseActivity<ActivityRunningResultBinding>(R.layout.activity_running_result) {

    override fun init() {
        initClickListener()

        AchievementDialog(this).show()
    }

    private fun initClickListener(){
        binding.apply {
            btnOk.setOnClickListener {
                finish()
            }
            btnRecommend.setOnClickListener {
                CreateRecommendDialog(this@RunningResultActivity).show()
            }
        }
    }
}