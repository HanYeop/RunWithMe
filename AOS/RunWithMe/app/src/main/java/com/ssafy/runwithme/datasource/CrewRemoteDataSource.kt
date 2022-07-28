package com.ssafy.runwithme.datasource

import com.ssafy.runwithme.api.CrewApi
import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.response.CreateRunRecordResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class CrewRemoteDataSource @Inject constructor(
    private val crewApi: CrewApi
){
    fun createRunRecords(crewId: Int, imgFile: MultipartBody.Part, runRecordDto: RequestBody
    ): Flow<BaseResponse<CreateRunRecordResponse>> = flow {
        emit(crewApi.createRunRecords(crewId, runRecordDto, imgFile))
    }
}