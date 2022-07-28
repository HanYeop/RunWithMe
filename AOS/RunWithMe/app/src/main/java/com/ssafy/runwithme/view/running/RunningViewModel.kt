package com.ssafy.runwithme.view.running

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.ssafy.runwithme.model.dto.RunRecordDto
import com.ssafy.runwithme.repository.CrewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import com.ssafy.runwithme.utils.Result
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class RunningViewModel @Inject constructor(
    private val crewRepository: CrewRepository
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
}