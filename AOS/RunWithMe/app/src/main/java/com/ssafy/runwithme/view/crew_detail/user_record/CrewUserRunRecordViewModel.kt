package com.ssafy.runwithme.view.crew_detail.user_record

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ssafy.runwithme.model.dto.RunRecordDto
import com.ssafy.runwithme.model.response.CrewBoardResponse
import com.ssafy.runwithme.repository.CrewActivityRepository
import com.ssafy.runwithme.repository.MyActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


@HiltViewModel
class CrewUserRunRecordViewModel @Inject constructor(
    private val crewActivityRepository: CrewActivityRepository,
) : ViewModel() {

    fun getCrewRecords(crewSeq: Int, size: Int): Flow<PagingData<RunRecordDto>> {
        return crewActivityRepository.getCrewRecords(crewSeq, size).cachedIn(viewModelScope)
    }

}