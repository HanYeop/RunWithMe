package com.ssafy.runwithme.datasource

import com.ssafy.runwithme.api.CrewManagerApi
import com.ssafy.runwithme.model.dto.MyCurrentCrewResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CrewManagerRemoteDataSource @Inject constructor(
    private val crewManagerApi: CrewManagerApi
){
    fun getMyCurrentCrew(): Flow<List<MyCurrentCrewResponse>> = flow {
        emit(crewManagerApi.getMyCurrentCrew())
    }
}