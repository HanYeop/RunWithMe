package com.ssafy.runwithme.view.home

import android.content.Intent
import android.content.SharedPreferences
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentHomeBinding
import com.ssafy.runwithme.model.response.MyCurrentCrewResponse
import com.ssafy.runwithme.utils.RUN_RECORD_CREW_ID
import com.ssafy.runwithme.utils.RUN_RECORD_CREW_NAME
import com.ssafy.runwithme.utils.RUN_RECORD_START_TIME
import com.ssafy.runwithme.view.running.RunningActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val homeViewModel by activityViewModels<HomeViewModel>()
    private lateinit var homeMyCurrentCrewAdapter: HomeMyCurrentCrewAdapter

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun init() {
        homeMyCurrentCrewAdapter = HomeMyCurrentCrewAdapter(listener)

        binding.apply {
            homeVM = homeViewModel
            recyclerMyCrewHorizon.adapter = homeMyCurrentCrewAdapter
        }

        homeViewModel.getMyCurrentCrew()

        initViewModelCallBack()

        initClickListener()
    }

    private fun initClickListener(){
        binding.apply {
            tvCrewMore.setOnClickListener { // 받은 데이터 그대로 이동
                findNavController().navigate(R.id.action_HomeFragment_to_myCurrentCrewFragment)
            }
            tvRankingMore.setOnClickListener {
                findNavController().navigate(R.id.action_HomeFragment_to_totalUserRankingFragment)
            }
        }
    }

    private fun runningStart(crewId: Int, crewName: String){
        sharedPreferences.edit().putLong(RUN_RECORD_START_TIME, System.currentTimeMillis()).apply()
        sharedPreferences.edit().putInt(RUN_RECORD_CREW_ID, crewId).apply()
        sharedPreferences.edit().putString(RUN_RECORD_CREW_NAME, crewName).apply()
        startActivity(Intent(requireContext(),RunningActivity::class.java))
    }

    private fun initViewModelCallBack(){
        homeViewModel.errorMsgEvent.observe(viewLifecycleOwner){
            showToast(it)
        }
    }

    private val listener : HomeMyCurrentCrewListener = object : HomeMyCurrentCrewListener {
        override fun onItemClick(myCurrentCrewResponse: MyCurrentCrewResponse) {
            val action = HomeFragmentDirections.actionHomeFragmentToCrewDetailFragment(myCurrentCrewResponse.crewDto, myCurrentCrewResponse.imageFileDto)
            findNavController().navigate(action)
        }

        override fun onBtnStartClick(myCurrentCrewResponse: MyCurrentCrewResponse) {
            runningStart(myCurrentCrewResponse.crewDto.crewSeq, myCurrentCrewResponse.crewDto.crewName)
        }
    }
}