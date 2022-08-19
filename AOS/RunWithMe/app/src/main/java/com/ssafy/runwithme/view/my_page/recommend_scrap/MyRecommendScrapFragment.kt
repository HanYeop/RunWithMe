package com.ssafy.runwithme.view.my_page.recommend_scrap

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentMyRecommendScrapBinding
import com.ssafy.runwithme.model.dto.RunRecordDto
import com.ssafy.runwithme.model.dto.TrackBoardDto
import com.ssafy.runwithme.view.recommend.RecommendDetailViewModel

class MyRecommendScrapFragment : BaseFragment<FragmentMyRecommendScrapBinding>(R.layout.fragment_my_recommend_scrap)  {

    private val recommendDetailViewModel by activityViewModels<RecommendDetailViewModel>()

    override fun init() {
        recommendDetailViewModel.getMyScrap(0)

        binding.apply {
            recommendDetailVM = recommendDetailViewModel
            recyclerMyScrap.adapter = MyRecommendScrapAdapter(listener)
        }

        initViewModelCallBack()

        initClickListener()
    }

    private fun initClickListener() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun initViewModelCallBack(){
        recommendDetailViewModel.successMsgEvent.observe(viewLifecycleOwner){
            showToast(it)
        }
        recommendDetailViewModel.errorMsgEvent.observe(viewLifecycleOwner){
            showToast(it)
        }
    }

    private val listener : MyRecommendScrapListener = object : MyRecommendScrapListener {
        override fun onItemClick(runRecordDto: RunRecordDto, trackBoardDto: TrackBoardDto, imgSeq : Int) {
            val action = MyRecommendScrapFragmentDirections.actionMyRecommendScrapFragmentToRecommendDetailFragment(runRecordDto, trackBoardDto, imgSeq)
            findNavController().navigate(action)
        }
    }

}