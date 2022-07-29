package com.ssafy.runwithme.view.crew_detail.board

import android.annotation.SuppressLint
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentCrewBoardBinding
import com.ssafy.runwithme.model.dto.CreateCrewBoardDto
import com.ssafy.runwithme.model.dto.CrewBoardDto
import com.ssafy.runwithme.utils.TAG
import com.ssafy.runwithme.view.crew_detail.CrewDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CrewBoardFragment : BaseFragment<FragmentCrewBoardBinding>(R.layout.fragment_crew_board) {

    private val crewBoardViewModel by viewModels<CrewBoardViewModel>()
    private val crewBoardAdapter = CrewBoardAdapter()
    private val args by navArgs<CrewBoardFragmentArgs>()

    private var crewSeq: Int = 0

    override fun init() {
        binding.apply {
            recyclerCrewBoard.adapter = crewBoardAdapter
        }

        crewSeq = args.crewid

        initClickListener()

        initViewModelCallback()
    }

    private fun initClickListener() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            fabCreateBoard.setOnClickListener {
                initCreateBoardDialog()
            }
        }
    }

    private fun initCreateBoardDialog(){
        val dialog = CreateCrewBoardDialog(requireContext(), createCrewBoardListener)
        dialog.show()
    }

    private val createCrewBoardListener : CreateCrewBoardListener = object : CreateCrewBoardListener {
        override fun onItemClick(content: String) {
            crewBoardViewModel.createCrewBoard(crewSeq, CreateCrewBoardDto(0, content))
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initViewModelCallback(){
        lifecycleScope.launch {
            crewBoardViewModel.getCrewBoards(args.crewid,10).collectLatest { pagingData ->
                crewBoardAdapter.submitData(pagingData)
                Log.d(TAG, "initViewModelCallback: first")
            }
        }

        crewBoardViewModel.errorMsgEvent.observe(viewLifecycleOwner){
            showToast(it)
        }

        crewBoardViewModel.failMsgEvent.observe(viewLifecycleOwner){
            showToast(it)
        }

        crewBoardViewModel.createSuccessMsgEvent.observe(viewLifecycleOwner){
            showToast(it)
            lifecycleScope.launch {
                crewBoardViewModel.getCrewBoards(args.crewid,10).collectLatest { pagingData ->
                    crewBoardAdapter.submitData(pagingData)
                    crewBoardAdapter.notifyDataSetChanged()
                    binding.recyclerCrewBoard.smoothScrollToPosition(0)
                    Log.d(TAG, "initViewModelCallback: createsuccessmsg")
                }
            }
        }
    }
}