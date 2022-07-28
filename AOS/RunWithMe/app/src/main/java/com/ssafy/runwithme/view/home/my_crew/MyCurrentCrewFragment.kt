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
import com.ssafy.runwithme.utils.*
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

    private val listener : MyCurrentCrewListener = object : MyCurrentCrewListener {
        override fun onItemClick(myCurrentCrewResponse: MyCurrentCrewResponse) {
            val action = MyCurrentCrewFragmentDirections.actionMyCurrentCrewFragmentToCrewDetailFragment(myCurrentCrewResponse.crewDto, myCurrentCrewResponse.imageFileDto)
            findNavController().navigate(action)
        }

        override fun onBtnStartClick(myCurrentCrewResponse: MyCurrentCrewResponse) {
            runningStart(sharedPreferences, myCurrentCrewResponse.crewDto.crewSeq, myCurrentCrewResponse.crewDto.crewName
                ,myCurrentCrewResponse.crewDto.crewGoalType, myCurrentCrewResponse.crewDto.crewGoalAmount)
            startActivity(Intent(requireContext(),RunningActivity::class.java))
        }
    }

    private fun initViewModelCallBack(){
        homeViewModel.errorMsgEvent.observe(viewLifecycleOwner){
            showToast(it)
        }
    }
}