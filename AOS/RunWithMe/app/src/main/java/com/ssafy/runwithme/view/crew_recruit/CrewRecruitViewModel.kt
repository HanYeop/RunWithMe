package com.ssafy.runwithme.view.crew_recruit

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
class CrewRecruitViewModel @Inject constructor(
    private val crewManagerRepository: CrewManagerRepository
) : ViewModel(){


    fun getRecruitCrew(size: Int) : Flow<PagingData<RecruitCrewResponse>> {
        return crewManagerRepository.getRecruitCrew(size).cachedIn(viewModelScope)
    }

}