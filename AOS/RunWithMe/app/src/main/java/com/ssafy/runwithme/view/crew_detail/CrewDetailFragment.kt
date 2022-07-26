package com.ssafy.runwithme.view.crew_detail

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentCrewDetailBinding
import com.ssafy.runwithme.model.response.MyCurrentCrewResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CrewDetailFragment : BaseFragment<FragmentCrewDetailBinding>(R.layout.fragment_crew_detail) {

    private val args by navArgs<CrewDetailFragmentArgs>()
    private lateinit var crewDetail: MyCurrentCrewResponse
    private val crewDetailViewModel by viewModels<CrewDetailViewModel>()

    override fun init() {
        initClickListener()

        crewDetail = args.mycrewdetail
        binding.crewDetail = crewDetail
    }

    private fun initClickListener(){
        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            tvMyRecordMore.setOnClickListener {
                findNavController().navigate(R.id.action_crewDetailFragment_to_crewMyRunRecordFragment)
            }

            tvRankingMore.setOnClickListener {
                findNavController().navigate(R.id.action_crewDetailFragment_to_crewUserRankingFragment)
            }

            // TEST
            tvBoardMore.setOnClickListener {
//                val action = CrewDetailFragmentDirections.actionCrewDetailFragmentToCrewBoardFragment(crewDetail.crewId)
                val action = CrewDetailFragmentDirections.actionCrewDetailFragmentToCrewBoardFragment(3)
                findNavController().navigate(action)
            }

            tvRankingMore.setOnClickListener {
                findNavController().navigate(R.id.action_crewDetailFragment_to_crewUserRankingFragment)
            }
        }
    }
}