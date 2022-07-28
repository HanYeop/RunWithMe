package com.ssafy.runwithme.view.home.my_crew

import android.content.Intent
import android.content.SharedPreferences
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentMyCurrentCrewBinding
import com.ssafy.runwithme.model.response.MyCurrentCrewResponse
import com.ssafy.runwithme.utils.RUN_RECORD_CREW_ID
import com.ssafy.runwithme.utils.RUN_RECORD_CREW_NAME
import com.ssafy.runwithme.utils.RUN_RECORD_START_TIME
import com.ssafy.runwithme.utils.Result
import com.ssafy.runwithme.view.home.HomeViewModel
import com.ssafy.runwithme.view.running.RunningActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MyCurrentCrewFragment
    : BaseFragment<FragmentMyCurrentCrewBinding>(R.layout.fragment_my_current_crew) {

    private val homeViewModel by activityViewModels<HomeViewModel>()
    private lateinit var myCurrentCrewAdapter : MyCurrentCrewAdapter

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun init() {
        myCurrentCrewAdapter = MyCurrentCrewAdapter(listener)
        binding.apply {
            homeVM = homeViewModel
            recyclerMyCurrentCrew.adapter = myCurrentCrewAdapter
        }
        initClickListener()

        initViewModelCallBack()
    }

    private fun initClickListener() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun runningStart(crewId: Int, crewName: String){
        sharedPreferences.edit().putLong(RUN_RECORD_START_TIME, System.currentTimeMillis()).apply()
        sharedPreferences.edit().putInt(RUN_RECORD_CREW_ID, crewId).apply()
        sharedPreferences.edit().putString(RUN_RECORD_CREW_NAME, crewName).apply()
        startActivity(Intent(requireContext(), RunningActivity::class.java))
    }

    private val listener : MyCurrentCrewListener = object : MyCurrentCrewListener {
        override fun onItemClick(myCurrentCrewResponse: MyCurrentCrewResponse) {
            val action = MyCurrentCrewFragmentDirections.actionMyCurrentCrewFragmentToCrewDetailFragment(myCurrentCrewResponse.crewDto, myCurrentCrewResponse.imageFileDto)
            findNavController().navigate(action)
        }

        override fun onBtnStartClick(myCurrentCrewResponse: MyCurrentCrewResponse) {
            runningStart(myCurrentCrewResponse.crewDto.crewSeq, myCurrentCrewResponse.crewDto.crewName)
        }
    }

    private fun initViewModelCallBack(){
        homeViewModel.errorMsgEvent.observe(viewLifecycleOwner){
            showToast(it)
        }
    }
}