package com.ssafy.runwithme.view.my_page.tab.total_record

import androidx.fragment.app.viewModels
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentMyTotalRunRecordBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyTotalRunRecordFragment : BaseFragment<FragmentMyTotalRunRecordBinding>(R.layout.fragment_my_total_run_record) {

    private val myTotalRunRecordViewModel by viewModels<MyTotalRunRecordViewModel>()

    override fun init() {
        myTotalRunRecordViewModel.getMyTotalRecord()
        binding.totalRunVM = myTotalRunRecordViewModel

        initViewModelCallBack()
    }

    private fun initViewModelCallBack(){
        myTotalRunRecordViewModel.errorMsgEvent.observe(viewLifecycleOwner){
            showToast(it)
        }
    }
}