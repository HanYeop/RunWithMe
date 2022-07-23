package com.ssafy.runwithme.view.home

import android.content.Intent
import androidx.navigation.fragment.findNavController
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentHomeBinding
import com.ssafy.runwithme.view.running.RunningActivity

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    override fun init() {
        initClickListener()
    }

    private fun initClickListener(){
        binding.apply {
            // TEST
            tvMyCurrentCrew.setOnClickListener {
                startActivity(Intent(requireContext(),RunningActivity::class.java))
            }
            tvCrewMore.setOnClickListener {
                findNavController().navigate(R.id.action_HomeFragment_to_myCurrentCrewFragment)
            }
        }
    }
}