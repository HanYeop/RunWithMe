package com.ssafy.runwithme.view.running.result

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseActivity
import com.ssafy.runwithme.databinding.ActivityRunningResultBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RunningResultActivity : BaseActivity<ActivityRunningResultBinding>(R.layout.activity_running_result) {

    private lateinit var navController : NavController


    override fun init() {
        initNavigation()
    }

    private fun initNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view_result) as NavHostFragment
        navController = navHostFragment.navController
    }
}