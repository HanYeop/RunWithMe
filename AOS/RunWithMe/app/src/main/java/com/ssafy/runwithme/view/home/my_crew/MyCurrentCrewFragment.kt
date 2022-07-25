package com.ssafy.runwithme.view.home.my_crew

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentMyCurrentCrewBinding
import com.ssafy.runwithme.model.response.MyCurrentCrewResponse
import com.ssafy.runwithme.view.home.HomeViewModel

class MyCurrentCrewFragment
    : BaseFragment<FragmentMyCurrentCrewBinding>(R.layout.fragment_my_current_crew), MyCurrentCrewListener {

    private val homeViewModel by activityViewModels<HomeViewModel>()
    private lateinit var myCurrentCrewAdapter : MyCurrentCrewAdapter

    override fun init() {
        myCurrentCrewAdapter = MyCurrentCrewAdapter(this)
        binding.apply {
            homeVM = homeViewModel
            recyclerMyCurrentCrew.adapter = myCurrentCrewAdapter
        }
        initClickListener()
    }

    private fun initClickListener() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

//    private val listener : MyCurrentCrewListener = object : MyCurrentCrewListener {
//        override fun onItemClick(myCurrentCrewResponse: MyCurrentCrewResponse) {
//            val action = MyCurrentCrewFragmentDirections.actionMyCurrentCrewFragmentToCrewDetailFragment(myCurrentCrewResponse)
//            findNavController().navigate(action)
//        }
//    }

    override fun onItemClick(myCurrentCrewResponse: MyCurrentCrewResponse) {
        val action = MyCurrentCrewFragmentDirections.actionMyCurrentCrewFragmentToCrewDetailFragment(myCurrentCrewResponse)
        findNavController().navigate(action)
    }
}