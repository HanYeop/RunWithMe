package com.ssafy.runwithme.view.home.ranking

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentTotalUserRankingBinding

class TotalUserRankingFragment : BaseFragment<FragmentTotalUserRankingBinding>(R.layout.fragment_total_user_ranking) {
    override fun init() {

        binding.spinnerTotalUserRanking.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // 1. 날짜 , 2. 시간, 3. 거리, 4. 평균 속도, 5. 칼로리 소모량
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

    }
}