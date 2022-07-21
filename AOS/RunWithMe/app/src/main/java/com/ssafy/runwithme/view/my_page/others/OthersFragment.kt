package com.ssafy.runwithme.view.my_page.others

import androidx.navigation.fragment.findNavController
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentOthersBinding

class OthersFragment : BaseFragment<FragmentOthersBinding>(R.layout.fragment_others) {
    override fun init() {
        initClickListener()
    }

    private fun initClickListener(){ // 뒤로가기 버튼
        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
}