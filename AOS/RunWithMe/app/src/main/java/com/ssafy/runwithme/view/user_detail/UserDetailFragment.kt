package com.ssafy.runwithme.view.user_detail

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentUserDetailBinding
import com.ssafy.runwithme.utils.TAG
import com.ssafy.runwithme.utils.achieve_list
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserDetailFragment : BaseFragment<FragmentUserDetailBinding>(R.layout.fragment_user_detail) {

    private val userDetailViewModel by viewModels<UserDetailViewModel>()
    private val args by navArgs<UserDetailFragmentArgs>()
    private var userSeq: Int = 0

    override fun init() {

        binding.apply {
            userDetailVM = userDetailViewModel
        }

        userSeq = args.userseq

        initClickListener()

        initViewModelCallBack()
    }

    private fun initClickListener(){
        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun initViewModelCallBack(){

        Log.d(TAG, "initViewModelCallBack: userSeq: $userSeq")
        userDetailViewModel.getUserProfile(userSeq)


        lifecycleScope.launch {
            userDetailViewModel.achieveList.collectLatest {
                binding.apply {
                    for (i in it) {
                        when (i) {
                            achieve_list[0] -> {
                                imageAchieveNumOne.alpha = 1f
                            }
                            achieve_list[1] -> {
                                imageAchieveNumTen.alpha = 1f
                            }
                            achieve_list[2] -> {
                                imageAchieveNumHundred.alpha = 1f
                            }
                            achieve_list[3] -> {
                                imageAchieveNumThousand.alpha = 1f
                            }
                            achieve_list[4] -> {
                                imageAchieveTimeTen.alpha = 1f
                            }
                            achieve_list[5] -> {
                                imageAchieveTimeHundred.alpha = 1f
                            }
                            achieve_list[6] -> {
                                imageAchieveTimeThousand.alpha = 1f
                            }
                            achieve_list[7] -> {
                                imageAchieveTimeTenThousand.alpha = 1f
                            }
                            achieve_list[8] -> {
                                imageAchieveDistanceTen.alpha = 1f
                            }
                            achieve_list[9] -> {
                                imageAchieveDistanceHundred.alpha = 1f
                            }
                            achieve_list[10] -> {
                                imageAchieveDistanceThousand.alpha = 1f
                            }
                            achieve_list[11] -> {
                                imageAchieveDistanceTenThousand.alpha = 1f
                            }
                        }
                    }
                }
            }
        }


        userDetailViewModel.errorMsgEvent.observe(viewLifecycleOwner){
            showToast(it)
        }
    }

}