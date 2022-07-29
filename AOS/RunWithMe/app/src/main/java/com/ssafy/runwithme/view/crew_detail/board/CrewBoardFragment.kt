package com.ssafy.runwithme.view.crew_detail.board

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentCrewBoardBinding
import com.ssafy.runwithme.model.dto.CreateCrewBoardDto
import com.ssafy.runwithme.model.response.CrewBoardResponse
import com.ssafy.runwithme.utils.TAG
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CrewBoardFragment : BaseFragment<FragmentCrewBoardBinding>(R.layout.fragment_crew_board) {

    private val crewBoardViewModel by viewModels<CrewBoardViewModel>()
    private lateinit var crewBoardAdapter : CrewBoardAdapter
    private val args by navArgs<CrewBoardFragmentArgs>()
    private lateinit var job : Job

    private var crewSeq: Int = 0

    override fun init() {
        Log.d(TAG, "init: userSeq: ${crewBoardViewModel.getUserSeq()}")
        crewBoardAdapter = CrewBoardAdapter(deleteCrewBoardListener, crewBoardViewModel.getUserSeq())
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

    private val deleteCrewBoardListener : CrewBoardDeleteListener = object : CrewBoardDeleteListener {
        override fun onItemClick(crewBoardResponse: CrewBoardResponse) {
            crewBoardViewModel.deleteCrewBoard(crewSeq, crewBoardResponse.crewBoardDto.crewBoardSeq)
        }
    }

    private fun initViewModelCallback(){
        job = lifecycleScope.launch {
            crewBoardViewModel.getCrewBoards(args.crewid,10).collectLatest { pagingData ->
                Log.d(TAG, "initViewModelCallback: first")
                crewBoardAdapter.submitData(pagingData)
            }
        }


        crewBoardViewModel.errorMsgEvent.observe(viewLifecycleOwner){
            showToast(it)
        }

        crewBoardViewModel.failMsgEvent.observe(viewLifecycleOwner){
            showToast(it)
        }

        crewBoardViewModel.successMsgEvent.observe(viewLifecycleOwner){
            showToast(it)
            updateList()

            lifecycleScope.launch {
                delay(300L)
                binding.recyclerCrewBoard.smoothScrollToPosition(0)
            }
        }
    }

    private fun updateList(){
        job.cancel()
        job = lifecycleScope.launch {
            crewBoardViewModel.getCrewBoards(args.crewid,10).collectLatest { pagingData ->
                Log.d(TAG, "initViewModelCallback: createsuccessmsg")
                crewBoardAdapter.submitData(pagingData)
            }
        }
    }
}