package com.ssafy.runwithme.view.home

import android.content.Intent
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentHomeBinding
import com.ssafy.runwithme.model.response.MyCurrentCrewResponse
import com.ssafy.runwithme.utils.Result
import com.ssafy.runwithme.view.home.my_crew.MyCurrentCrewFragmentDirections
import com.ssafy.runwithme.view.running.RunningActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val homeViewModel by activityViewModels<HomeViewModel>()
    private lateinit var homeMyCurrentCrewAdapter: HomeMyCurrentCrewAdapter

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
            // TEST
            tvMyCurrentCrew.setOnClickListener {
                startActivity(Intent(requireContext(),RunningActivity::class.java))
            }
            tvCrewMore.setOnClickListener { // 받은 데이터 그대로 이동
                findNavController().navigate(R.id.action_HomeFragment_to_myCurrentCrewFragment)
            }
            tvRankingMore.setOnClickListener {
                findNavController().navigate(R.id.action_HomeFragment_to_totalUserRankingFragment)
            }
        }
    }

    private fun initViewModelCallBack(){
        homeViewModel.errorMsgEvent.observe(viewLifecycleOwner){
            showToast(it)
        }
        lifecycleScope.launch {
            homeViewModel.myCurrentCrewList.collectLatest {
                if(it is Result.Success){
                    homeMyCurrentCrewAdapter.submitList(it.data.data)
                }
            }
        }
    }

    private val listener : HomeMyCurrentCrewListener = object : HomeMyCurrentCrewListener {
        override fun onItemClick(myCurrentCrewResponse: MyCurrentCrewResponse) {
            val action = HomeFragmentDirections.actionHomeFragmentToCrewDetailFragment(myCurrentCrewResponse.crewDto, myCurrentCrewResponse.imageFileDto)
            findNavController().navigate(action)
        }
    }
}