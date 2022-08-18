package com.ssafy.runwithme.datasource

import com.ssafy.runwithme.api.CrewApi
import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.dto.CoordinateDto
import com.ssafy.runwithme.model.dto.CrewDto
import com.ssafy.runwithme.model.dto.PasswordDto
import com.ssafy.runwithme.model.response.CreateRunRecordResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Path
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CrewRemoteDataSource @Inject constructor(
    private val crewApi: CrewApi
){
    fun createRunRecords(crewId: Int, imgFile: MultipartBody.Part, runRecordDto: RequestBody
    ): Flow<BaseResponse<CreateRunRecordResponse>> = flow {
        emit(crewApi.createRunRecords(crewId, runRecordDto, imgFile))
    }

    fun joinCrew(crewId: Int, password: PasswordDto?): Flow<BaseResponse<CrewDto>> = flow {
        if(password != null){
            emit(crewApi.joinCrew(crewId, password))
        }else{
            emit(crewApi.joinCrew(crewId))
        }
    }

    fun createCoordinates(recordSeq: Int, coordinates: List<CoordinateDto>): Flow<BaseResponse<String>> = flow {
        emit(crewApi.createCoordinates(recordSeq, coordinates))
    }

    fun getCoordinates(recordSeq: Int): Flow<BaseResponse<List<CoordinateDto>>> = flow {
        emit(crewApi.getCoordinates(recordSeq))
    }
}