package com.ssafy.runwithme.datasource

import com.ssafy.runwithme.api.CrewActivityApi
import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.dto.CreateCrewBoardDto
import com.ssafy.runwithme.model.dto.CrewBoardDto
import com.ssafy.runwithme.model.dto.RunRecordDto
import com.ssafy.runwithme.model.response.CrewBoardResponse
import com.ssafy.runwithme.model.response.RankingResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CrewActivityRemoteDataSource @Inject constructor(
    private val crewActivityApi: CrewActivityApi
){

    fun getCrewRanking(crewSeq: Int, type: String) : Flow<BaseResponse<List<RankingResponse>>> = flow {
        emit(crewActivityApi.getCrewRanking(crewSeq, type))
    }

    fun createCrewBoard(crewSeq: Int, crewBoardDto: CreateCrewBoardDto): Flow<BaseResponse<CrewBoardResponse>> = flow {
        emit(crewActivityApi.createCrewBoard(crewSeq, crewBoardDto))
    }

    fun deleteCrewBoard(crewSeq: Int, boardSeq: Int): Flow<BaseResponse<Boolean>> = flow {
        emit(crewActivityApi.deleteCrewBoard(crewSeq, boardSeq))
    }

}