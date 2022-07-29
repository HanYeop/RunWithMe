package com.ssafy.runwithme.datasource

import com.ssafy.runwithme.api.CrewActivityApi
import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.dto.CreateCrewBoardDto
import com.ssafy.runwithme.model.dto.CrewBoardDto
import com.ssafy.runwithme.model.dto.RunRecordDto
import com.ssafy.runwithme.model.response.CrewBoardResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CrewBoardsDataSource @Inject constructor(
    private val crewActivityApi: CrewActivityApi
){

    fun getCrewBoardsTop3(crewSeq: Int, size: Int): Flow<BaseResponse<List<CrewBoardResponse>>> = flow {
        emit(crewActivityApi.getCrewBoardsTop3(crewSeq, size))
    }

    fun createCrewBoard(crewSeq: Int, crewBoardDto: CreateCrewBoardDto): Flow<BaseResponse<CrewBoardResponse>> = flow {
        emit(crewActivityApi.createCrewBoard(crewSeq, crewBoardDto))
    }

}