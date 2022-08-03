package com.ssafy.runwithme.view.running.list

import android.content.Intent
import android.content.SharedPreferences
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseActivity
import com.ssafy.runwithme.databinding.ActivityRunningListBinding
import com.ssafy.runwithme.model.response.MyCurrentCrewResponse
import com.ssafy.runwithme.utils.runningStart
import com.ssafy.runwithme.view.home.my_crew.MyCurrentCrewAdapter
import com.ssafy.runwithme.view.running.RunningActivity
import com.ssafy.runwithme.view.running.RunningViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RunningListActivity : BaseActivity<ActivityRunningListBinding>(R.layout.activity_running_list) {

    private val runningViewModel by viewModels<RunningViewModel>()
    private lateinit var runningListAdapter: RunningListAdapter

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun init() {
        runningListAdapter = RunningListAdapter(listener)

        binding.apply {
            runningVM = runningViewModel
            recyclerRunningCrewList.adapter = runningListAdapter
        }

        initClickListener()

        initViewModelCallBack()

        runningViewModel.getMyCurrentCrew()
    }

    val listener = object : RunningListListener{
        override fun onItemClick(myCurrentCrewResponse: MyCurrentCrewResponse) {
            runningViewModel.getMyProfile()
            runningStart(sharedPreferences, myCurrentCrewResponse.crewDto.crewSeq, myCurrentCrewResponse.crewDto.crewName
                ,myCurrentCrewResponse.crewDto.crewGoalType, myCurrentCrewResponse.crewDto.crewGoalAmount)
            startActivity(Intent(this@RunningListActivity, RunningActivity::class.java))
            finish()
        }
    }

    private fun initClickListener(){
        binding.apply {
            toolbar.setNavigationOnClickListener {
                finish()
            }
        }
    }

    private fun initViewModelCallBack(){
        runningViewModel.errorMsgEvent.observe(this){
            showToast(it)
        }
    }
}