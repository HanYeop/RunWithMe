package com.ssafy.runwithme.view.my_page.tab.achievement

import androidx.fragment.app.viewModels
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentAchievementBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AchievementFragment : BaseFragment<FragmentAchievementBinding>(R.layout.fragment_achievement) {

    private val achievementViewModel by viewModels<AchievementViewModel>()

    override fun init() {
        achievementViewModel.getMyAchieve()
    }

}