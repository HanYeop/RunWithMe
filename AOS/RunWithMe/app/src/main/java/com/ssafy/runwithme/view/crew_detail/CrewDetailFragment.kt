package com.ssafy.runwithme.view.crew_detail

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentCrewDetailBinding
import com.ssafy.runwithme.model.dto.CrewDto
import com.ssafy.runwithme.model.dto.ImageFileDto
import com.ssafy.runwithme.utils.TAG
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CrewDetailFragment : BaseFragment<FragmentCrewDetailBinding>(R.layout.fragment_crew_detail) {

    private val args by navArgs<CrewDetailFragmentArgs>()
    private lateinit var crewDto: CrewDto
    private lateinit var imageFileDto: ImageFileDto
    private val crewDetailViewModel by viewModels<CrewDetailViewModel>()
    private lateinit var crewRunRecordAdapter : CrewDetailRunRecordTop3Adapter
    private lateinit var crewBoardsAdapter : CrewDetailBoardsTop3Adapter

    override fun init() {
        crewRunRecordAdapter = CrewDetailRunRecordTop3Adapter(crewRunRecordListener)
        crewBoardsAdapter = CrewDetailBoardsTop3Adapter(crewBoardsListener)
        binding.apply {
            recyclerCrewRecord.adapter = crewRunRecordAdapter
            recyclerCrewBoard.adapter = crewBoardsAdapter
        }

        initClickListener()

        crewDto = args.crewdto
        imageFileDto = args.imagefiledto

        binding.crewDto = crewDto
        binding.imageFileDto = imageFileDto

        initViewModelCallback()
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

            tvBoardMore.setOnClickListener {
                val action = CrewDetailFragmentDirections.actionCrewDetailFragmentToCrewBoardFragment(crewDto!!.crewSeq)
                findNavController().navigate(action)
            }

            tvRankingMore.setOnClickListener {
                findNavController().navigate(R.id.action_crewDetailFragment_to_crewUserRankingFragment)
            }
        }
    }

    private fun initViewModelCallback(){
        crewDetailViewModel.getRunRecordsTop3(crewDto.crewSeq.toString(), 3)

        crewDetailViewModel.getCrewBoardsTop3(crewDto.crewSeq, 3)

        lifecycleScope.launch {
            crewDetailViewModel.crewRunRecordList.collectLatest {
                crewRunRecordAdapter.submitList(it)
            }
        }

        lifecycleScope.launch {
            crewDetailViewModel.crewBoardsList.collectLatest {
                crewBoardsAdapter.submitList(it)
            }
        }

        crewDetailViewModel.errorMsgEvent.observe(viewLifecycleOwner){
            showToast(it)
        }
    }

    private val crewRunRecordListener: CrewRunRecordListener = object : CrewRunRecordListener {
        override fun onItemClick(crewSeq: Int) {
            val action = CrewDetailFragmentDirections.actionCrewDetailFragmentToCrewUserRunRecordFragment(crewSeq)
            findNavController().navigate(action)
        }
    }

    private val crewBoardsListener: CrewBoardsListener = object : CrewBoardsListener {
        override fun onItemClick(crewSeq: Int) {
            val action = CrewDetailFragmentDirections.actionCrewDetailFragmentToCrewBoardFragment(crewSeq)
            findNavController().navigate(action)
        }
    }
}