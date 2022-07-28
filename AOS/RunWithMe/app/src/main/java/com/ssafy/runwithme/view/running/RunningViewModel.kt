package com.ssafy.runwithme.view.running

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.ssafy.runwithme.model.dto.RunRecordDto
import com.ssafy.runwithme.repository.CrewRepository
import com.ssafy.runwithme.utils.FormDataUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class RunningViewModel @Inject constructor(
    private val crewRepository: CrewRepository
): ViewModel(){

    fun createRunRecord(crewId: Int, imgFile: MultipartBody.Part, runRecordDto: RunRecordDto){
        val json = Gson().toJson(runRecordDto)
        val run = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), json)

        Log.d("test5", "createRunRecord: $imgFile $run")

        viewModelScope.launch(Dispatchers.IO) {
            Log.d("test5", "createRunRecord: ")
            crewRepository.createRunRecords(crewId, imgFile, run).collectLatest {
                Log.d("test5", "createRunRecord: $it")
            }
        }
    }
}