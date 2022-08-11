package com.ssafy.runwithme.view.crew_recruit.detail

import android.content.SharedPreferences
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentCrewRecruitDetailBinding
import com.ssafy.runwithme.model.dto.CrewDto
import com.ssafy.runwithme.model.dto.ImageFileDto
import com.ssafy.runwithme.utils.USER
import com.ssafy.runwithme.view.crew_detail.CrewDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CrewRecruitDetailFragment : BaseFragment<FragmentCrewRecruitDetailBinding>(R.layout.fragment_crew_recruit_detail) {

    @Inject
    lateinit var sharedPref: SharedPreferences
    private val args by navArgs<CrewRecruitDetailFragmentArgs>()
    private lateinit var crewDto: CrewDto
    private lateinit var imageFileDto: ImageFileDto
    private val crewDetailViewModel by viewModels<CrewDetailViewModel>()
    private var mySeq: Int = 0

    override fun init() {
        crewDto = args.crewDto
        imageFileDto = args.imagefiledto

        mySeq = sharedPref.getString(USER, "0")!!.toInt()

        binding.apply {
            crewDetailVM = crewDetailViewModel
            crewDto = args.crewDto
            imageFileDto = args.imagefiledto
        }

        initClickListener()

        initViewModelCallback()
    }

    private fun initClickListener() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            btnJoinCrew.setOnClickListener {
                crewDetailViewModel.joinCrew(crewDto!!.crewSeq, null)
            }

            btnResignCrew.setOnClickListener {
                if(crewDetailViewModel.isCrewManager.value){
                    crewDetailViewModel.deleteCrew(crewDto!!.crewSeq)
                }else{
                    crewDetailViewModel.resignCrew(crewDto!!.crewSeq)
                }
                findNavController().popBackStack()
            }
        }
    }

    private fun initViewModelCallback() {
        crewDetailViewModel.setState(crewDto.crewDateStart, crewDto.crewDateEnd, crewDto.crewTimeStart, crewDto.crewTimeEnd)

        crewDetailViewModel.checkCrewMember(crewDto.crewSeq)

        crewDetailViewModel.checkCrewManager(crewDto.crewManagerSeq == mySeq)

        crewDetailViewModel.successMsgEvent.observe(viewLifecycleOwner) {
            showToast(it)
            binding.apply {
                btnJoinCrew.visibility = View.GONE
                btnResignCrew.visibility = View.VISIBLE
            }
        }

        crewDetailViewModel.errorMsgEvent.observe(viewLifecycleOwner) {
            showToast(it)
        }
    }

}