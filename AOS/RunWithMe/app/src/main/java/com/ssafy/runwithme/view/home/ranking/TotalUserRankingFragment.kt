package com.ssafy.runwithme.view.home.ranking

import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentTotalUserRankingBinding
import com.ssafy.runwithme.view.home.HomeViewModel

class TotalUserRankingFragment : BaseFragment<FragmentTotalUserRankingBinding>(R.layout.fragment_total_user_ranking) {

    private val homeViewModel by activityViewModels<HomeViewModel>()

    override fun init() {
        binding.apply {
            homeVM = homeViewModel
            myRank = homeViewModel.myRanking.value
            recyclerTotalUserRanking.adapter = TotalRankingAdapter(homeViewModel)
        }

        initClickListener()

        binding.spinnerTotalUserRanking.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                // 1. 거리 , 2. 시간, 3. 포인트
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }


    private fun initClickListener() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

}