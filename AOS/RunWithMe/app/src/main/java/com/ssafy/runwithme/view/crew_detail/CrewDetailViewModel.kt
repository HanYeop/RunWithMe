package com.ssafy.runwithme.view.crew_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ssafy.runwithme.model.dto.CrewBoardResponse
import com.ssafy.runwithme.repository.CrewActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class CrewDetailViewModel @Inject constructor(
    private val crewActivityRepository: CrewActivityRepository
) : ViewModel(){

    fun getCrewBoards(crewId: Int, size: Int): Flow<PagingData<CrewBoardResponse>> {
        return crewActivityRepository.getCrewBoards(crewId, size).cachedIn(viewModelScope)
    }
}