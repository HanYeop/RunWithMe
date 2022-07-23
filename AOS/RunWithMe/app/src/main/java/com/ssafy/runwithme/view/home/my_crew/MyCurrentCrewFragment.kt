package com.ssafy.runwithme.view.home.my_crew

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentMyCurrentCrewBinding
import com.ssafy.runwithme.view.home.HomeViewModel

class MyCurrentCrewFragment : BaseFragment<FragmentMyCurrentCrewBinding>(R.layout.fragment_my_current_crew) {

    private val homeViewModel by activityViewModels<HomeViewModel>()
    private val myCurrentCrewAdapter = MyCurrentCrewAdapter()

    override fun init() {
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
}