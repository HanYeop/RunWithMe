package com.ssafy.runwithme.view.my_page.recommend_scrap

import androidx.navigation.fragment.findNavController
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentMyRecommendScrapBinding

class MyRecommendScrapFragment : BaseFragment<FragmentMyRecommendScrapBinding>(R.layout.fragment_my_recommend_scrap)  {

    override fun init() {
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