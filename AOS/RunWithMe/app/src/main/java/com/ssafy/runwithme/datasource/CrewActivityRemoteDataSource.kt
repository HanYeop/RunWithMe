package com.ssafy.runwithme.datasource

import com.ssafy.runwithme.api.CrewActivityApi
import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.dto.CreateCrewBoardDto
import com.ssafy.runwithme.model.dto.RunRecordDto
import com.ssafy.runwithme.model.response.CrewBoardResponse
import com.ssafy.runwithme.model.response.CrewMyTotalRecordDataResponse
import com.ssafy.runwithme.model.response.MyGraphDataResponse
import com.ssafy.runwithme.model.response.RankingResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CrewActivityRemoteDataSource @Inject constructor(
    private val crewActivityApi: CrewActivityApi
){

    fun getCrewRanking(crewSeq: Int, type: String) : Flow<BaseResponse<List<RankingResponse>>> = flow {
        emit(crewActivityApi.getCrewRanking(crewSeq, type))
    }

    fun createCrewBoard(crewSeq: Int, crewBoardDto: RequestBody, img: MultipartBody.Part?): Flow<BaseResponse<CrewBoardResponse>> = flow {
        if(img == null){
            emit(crewActivityApi.createCrewBoard(crewSeq, crewBoardDto, img))
        } else {
            emit(crewActivityApi.createCrewBoard(crewSeq, crewBoardDto))
        }
    }

    fun deleteCrewBoard(boardSeq: Int): Flow<BaseResponse<Boolean>> = flow {
        emit(crewActivityApi.deleteCrewBoard(boardSeq))
    }

    fun getMyGraphData(crewSeq: Int, goalType: String): Flow<BaseResponse<List<MyGraphDataResponse>>> = flow {
        emit(crewActivityApi.getMyGraphData(crewSeq, goalType))
    }

    fun getMyTotalRecordData(crewSeq: Int): Flow<BaseResponse<CrewMyTotalRecordDataResponse>> = flow {
        emit(crewActivityApi.getMyTotalRecordData(crewSeq))
    }

    fun getMyRunrecord(crewSeq: Int) : Flow<BaseResponse<List<RunRecordDto>>> = flow {
        emit(crewActivityApi.getMyRunrecord(crewSeq))
    }

}