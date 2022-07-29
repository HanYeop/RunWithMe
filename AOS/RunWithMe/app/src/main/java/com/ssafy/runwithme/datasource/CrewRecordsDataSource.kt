package com.ssafy.runwithme.datasource

import com.ssafy.runwithme.api.CrewActivityApi
import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.dto.RunRecordDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CrewRecordsDataSource  @Inject constructor(
    private val crewActivityApi: CrewActivityApi
){

    fun getCrewRecordsTop3(crewSeq: String, size: Int): Flow<BaseResponse<List<RunRecordDto>>> = flow {
        emit(crewActivityApi.getCrewRecordsTop3(crewSeq, size))
    }

}