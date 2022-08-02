package com.ssafy.runwithme.view.running

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.ssafy.runwithme.model.dto.RunRecordDto
import com.ssafy.runwithme.repository.CrewRepository
import com.ssafy.runwithme.repository.MyActivityRepository
import com.ssafy.runwithme.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class RunningViewModel @Inject constructor(
    private val crewRepository: CrewRepository,
    private val sharedPreferences: SharedPreferences,
    private val myActivityRepository: MyActivityRepository
): ViewModel(){

    private val _runRecordSeq = MutableStateFlow(0)
    val runRecordSeq get() = _runRecordSeq.asStateFlow()

    fun createRunRecord(crewId: Int, imgFile: MultipartBody.Part, runRecordDto: RunRecordDto){
        val json = Gson().toJson(runRecordDto)
        val run = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), json)

        viewModelScope.launch(Dispatchers.IO) {
            crewRepository.createRunRecords(crewId, imgFile, run).collectLatest {
                if(it is Result.Success){
                    _runRecordSeq.value = it.data.data.runRecordDto.runRecordSeq
                }
            }
        }
    }

    fun getMyProfile(){
        viewModelScope.launch(Dispatchers.IO) {
            myActivityRepository.getMyProfile().collectLatest {
                if(it is Result.Success){
                    sharedPreferences.edit().putInt(USER_WEIGHT,it.data.data.userDto.weight).apply()
                    sharedPreferences.edit().putString(USER_NAME, it.data.data.userDto.nickName).apply()
                } else if(it is Result.Error){

                }
            }
        }
    }
}