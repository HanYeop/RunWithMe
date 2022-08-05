package com.ssafy.runwithme.view.my_page.tab.achievement

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentAchievementBinding
import com.ssafy.runwithme.model.dto.CrewDto
import com.ssafy.runwithme.model.dto.ImageFileDto
import com.ssafy.runwithme.utils.achieve_list
import com.ssafy.runwithme.view.my_page.MyPageFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AchievementFragment : BaseFragment<FragmentAchievementBinding>(R.layout.fragment_achievement) {

    private val achievementViewModel by viewModels<AchievementViewModel>()
    private lateinit var endCrewAdapter: EndCrewAdapter

    override fun init() {
        initViewModelCallBack()

        binding.achieveVM = achievementViewModel
        endCrewAdapter = EndCrewAdapter(listener)
        binding.recyclerMyCrewHistory.adapter = endCrewAdapter

        achievementViewModel.getMyAchieve()

        achievementViewModel.getMyEndCrew()
    }

    private val listener : EndCrewListener = object : EndCrewListener {
        override fun onItemClick(crewDto: CrewDto, imgDto : ImageFileDto) {
            val action = MyPageFragmentDirections.actionMyPageFragmentToCrewDetailFragment(crewDto, imgDto)
            findNavController().navigate(action)
        }
    }

    private fun initViewModelCallBack(){
        lifecycleScope.launch {
            achievementViewModel.achieveList.collectLatest {
                Log.d("test5", "initViewModelCallBack: $it")
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

        achievementViewModel.errorMsgEvent.observe(viewLifecycleOwner){
            showToast(it)
        }
    }

}