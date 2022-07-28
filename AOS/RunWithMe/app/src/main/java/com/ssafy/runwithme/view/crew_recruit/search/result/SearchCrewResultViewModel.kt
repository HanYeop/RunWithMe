package com.ssafy.runwithme.view.crew_recruit.search.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ssafy.runwithme.model.response.RecruitCrewResponse
import com.ssafy.runwithme.repository.CrewManagerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class SearchCrewResultViewModel @Inject constructor(
    private val crewManagerRepository: CrewManagerRepository
) : ViewModel() {


    fun getSearchResultCrew(
        size: Int, crewName: String?, startDay: String?,
        endDay: String?,
        startTime: String?,
        endTime: String?,
        minCost: Int,
        maxCost: Int,
        purposeMinValue: Int,
        purposeMaxValue: Int,
        goalMinDay: Int,
        goalMaxDay: Int,
        purposeType: String?
    ): Flow<PagingData<RecruitCrewResponse>> {
        return crewManagerRepository.getSearchResultCrew(
            size, crewName, startDay,
            endDay,
            startTime,
            endTime,
            minCost,
            maxCost,
            purposeMinValue,
            purposeMaxValue,
            goalMinDay,
            goalMaxDay,
            purposeType
        ).cachedIn(viewModelScope)
    }

}