package com.ssafy.runwithme.view.my_page.tab.my_board

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentMyBoardBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MyBoardFragment : BaseFragment<FragmentMyBoardBinding>(R.layout.fragment_my_board) {

    private val myBoardViewModel by viewModels<MyBoardViewModel>()
    private val myBoardAdapter = MyBoardAdapter()

    override fun init() {
        binding.apply {
            recyclerMyBoard.adapter = myBoardAdapter

        }

        initClickListener()

        initViewModelCallback()
    }

    private fun initClickListener() {
        binding.apply { // 게시물 클릭시 해당 게시물 크루로 이동?

        }
    }

    private fun initViewModelCallback(){
        lifecycleScope.launch {
            myBoardViewModel.getMyBoards(10).collectLatest { pagingData ->
                myBoardAdapter.submitData(pagingData)
            }
        }
    }
}