package com.ssafy.runwithme.view.crew_recruit.create

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentCreateCrew4Binding
import com.ssafy.runwithme.view.crew_recruit.CostDialog
import com.ssafy.runwithme.view.crew_recruit.CostDialogListener
import dagger.hilt.android.AndroidEntryPoint

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class CreateCrewFragment4 : BaseFragment<FragmentCreateCrew4Binding>(R.layout.fragment_create_crew4) {

    private val createCrewViewModel by activityViewModels<CreateCrewViewModel>()

    override fun init() {

        binding.apply {
            crewCreateVM = createCrewViewModel
        }

        initClickListener()

    }

    @SuppressLint("ResourceAsColor")
    private fun initClickListener() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            btnCreateNext.setOnClickListener {
                findNavController().navigate(R.id.action_createCrewFragment4_to_createCrewFragment5)
            }

            btnCreateCrewMaxMember.setOnClickListener {
                initMaxMemberDialog()
            }

            btnCreateCrewCost.setOnClickListener {
                initCostDialog()
            }

        }
    }

    private fun initCostDialog() {
        val costDialog = CostDialog(requireContext(), costDialogListener)
        costDialog.show()
    }

    private fun initMaxMemberDialog() {
        val maxMemberDialog = MaxMemberDialog(requireContext(), maxMemberDialogListener)
        maxMemberDialog.show()
    }

    private val maxMemberDialogListener: MaxMemberDialogListener = object : MaxMemberDialogListener {
        override fun onItemClick(max: Int) {
            createCrewViewModel.setMaxMember(max)
        }
    }

    private val costDialogListener : CostDialogListener = object : CostDialogListener {
        override fun onItemClick(cost: String) {
            createCrewViewModel.setCost(cost)
        }
    }

}