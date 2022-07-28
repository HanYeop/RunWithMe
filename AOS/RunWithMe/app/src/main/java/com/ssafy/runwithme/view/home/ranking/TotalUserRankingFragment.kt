package com.ssafy.runwithme.view.home.ranking

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentTotalUserRankingBinding
import com.ssafy.runwithme.view.home.HomeViewModel

class TotalUserRankingFragment : BaseFragment<FragmentTotalUserRankingBinding>(R.layout.fragment_total_user_ranking) {

    private val homeViewModel by activityViewModels<HomeViewModel>()

    override fun init() {
        // binding.myRank = homeViewModel.myRanking

        initClickListener()

        binding.spinnerTotalUserRanking.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // 1. 거리 , 2. 시간, 3. 포인트, 4. 평균 속도, 5. 칼로리 소모량
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