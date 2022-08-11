package com.ssafy.runwithme.view.crew_recruit.create

import android.annotation.SuppressLint
import android.os.Build
import android.view.View
import android.widget.RadioGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseFragment
import com.ssafy.runwithme.databinding.FragmentCreateCrew3Binding
import dagger.hilt.android.AndroidEntryPoint

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class CreateCrewFragment3 : BaseFragment<FragmentCreateCrew3Binding>(R.layout.fragment_create_crew3) {

    private val createCrewViewModel by activityViewModels<CreateCrewViewModel>()

    override fun init() {

        binding.apply {
            crewCreateVM = createCrewViewModel
        }

        initClickListener()

        initViewModelCallBack()

        initRadioGroupCheck()

    }

    @SuppressLint("ResourceAsColor")
    private fun initClickListener() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            btnCreateNext.setOnClickListener {
                findNavController().navigate(R.id.action_createCrewFragment3_to_createCrewFragment4)
            }

            btnCreateCrewPasswd.setOnClickListener {
                initPasswdDialog()
            }

        }
    }


    private fun initRadioGroupCheck() {
        binding.apply {
            radioGroupPasswd.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener{
                @SuppressLint("ResourceAsColor")
                override fun onCheckedChanged(radioGroup: RadioGroup?, checkId: Int) {
                    when(checkId){
                        R.id.radio_btn_passwd_off ->{
                            binding.apply {
                                btnCreateCrewPasswd.visibility = View.GONE
                                tvPasswd.visibility = View.GONE
                                createCrewViewModel.setPasswd("")
                                createCrewViewModel.isSettingPasswd = false
                            }
                        }

                        R.id.radio_btn_passwd_on -> {
                            binding.apply {
                                btnCreateCrewPasswd.visibility = View.VISIBLE
                                tvPasswd.visibility = View.VISIBLE
                                createCrewViewModel.isSettingPasswd = true
                            }
                        }
                    }
                }
            })

        }
    }

    private fun initPasswdDialog() {
        val passwdDialog = PasswdDialog(requireContext(), passwdDialogListener)
        passwdDialog.show()
    }

    private val passwdDialogListener: PasswdDialogListener = object : PasswdDialogListener {
        override fun onItemClick(passwd: String) {
            createCrewViewModel.setPasswd(passwd)
        }
    }

    private fun initViewModelCallBack() {
        createCrewViewModel.failMsgEvent.observe(viewLifecycleOwner) {
            showToast(it)
        }
    }

}