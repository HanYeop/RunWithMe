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
import com.ssafy.runwithme.databinding.FragmentCreateCrew5Binding
import com.ssafy.runwithme.view.loading.LoadingDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class CreateCrewFragment5 : BaseFragment<FragmentCreateCrew5Binding>(R.layout.fragment_create_crew5) {

    private val createCrewViewModel by activityViewModels<CreateCrewViewModel>()
    private lateinit var loadingDialog: LoadingDialog

    override fun init() {
        loadingDialog = LoadingDialog(requireContext())

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

            btnCreate.setOnClickListener {
                createCrewViewModel.createCrew()
                loading()
            }

            btnCreateCrewPasswd.setOnClickListener {
                initPasswdDialog()
            }

        }
    }

    private fun loading(){
        loadingDialog.show()
        // 로딩이 진행되지 않았을 경우
        CoroutineScope(Dispatchers.Main).launch {
            delay(1500)
            if(loadingDialog.isShowing){
                loadingDialog.dismiss()
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
        createCrewViewModel.errorMsgEvent.observe(viewLifecycleOwner) {
            showToast(it)
        }

        createCrewViewModel.successMsgEvent.observe(viewLifecycleOwner) {
            loadingDialog.dismiss()
            showToast(it)
            createCrewViewModel.refresh()
            findNavController().navigate(R.id.action_createCrewFragment5_to_CrewRecruitFragment)
        }

        createCrewViewModel.failMsgEvent.observe(viewLifecycleOwner) {
            showToast(it)
        }
    }

}