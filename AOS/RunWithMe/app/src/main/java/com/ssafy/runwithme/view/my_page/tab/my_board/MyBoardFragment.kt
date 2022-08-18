package com.ssafy.runwithme.view.my_page.tab.my_board

import android.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentMyBoardBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyBoardFragment : BaseFragment<FragmentMyBoardBinding>(R.layout.fragment_my_board) {

    private val myBoardViewModel by viewModels<MyBoardViewModel>()
    private lateinit var myBoardAdapter : MyBoardAdapter
    private lateinit var job : Job

    override fun init() {
        myBoardAdapter = MyBoardAdapter(deleteDialogListener)

        binding.apply {
            recyclerMyBoard.adapter = myBoardAdapter
        }

        initViewModelCallback()
    }

    private fun initViewModelCallback(){
        job = lifecycleScope.launch {
            myBoardViewModel.getMyBoards(10).collectLatest { pagingData ->
                myBoardAdapter.submitData(pagingData)
            }
        }

        myBoardViewModel.successMsgEvent.observe(viewLifecycleOwner){
            showToast(it)
            updateList()

            lifecycleScope.launch {
                delay(300L)
                binding.recyclerMyBoard.smoothScrollToPosition(0)
            }
        }
        myBoardViewModel.errorMsgEvent.observe(viewLifecycleOwner){
            showToast(it)
        }
        myBoardViewModel.failMsgEvent.observe(viewLifecycleOwner){
            showToast(it)
        }
    }

    private val deleteDialogListener : DeleteDialogListener = object : DeleteDialogListener {
        override fun onItemClick(boardSeq: Int) {
            AlertDialog.Builder(requireContext())
                .setTitle("작성글을 삭제하시겠습니까?")
                .setPositiveButton("확인") { _, _ ->
                    myBoardViewModel.deleteCrewBoard(boardSeq)
                }
                .setNegativeButton("취소") { _, _ -> }
                .show()
        }
    }

    private fun updateList(){
        job.cancel()
        job = lifecycleScope.launch {
            myBoardViewModel.getMyBoards(10).collectLatest { pagingData ->
                myBoardAdapter.submitData(pagingData)
            }
        }
    }
}