package com.ssafy.runwithme.repository

import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.datasource.CrewRemoteDataSource
import com.ssafy.runwithme.model.response.CreateRunRecordResponse
import com.ssafy.runwithme.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class CrewRepository @Inject constructor(
    private val crewRemoteDataSource: CrewRemoteDataSource) {

    fun createRunRecords(imgFile: MultipartBody.Part, runRecordDto: RequestBody): Flow<Result<BaseResponse<CreateRunRecordResponse>>> = flow {
        emit(Result.Loading)
        crewRemoteDataSource.createRunRecords(imgFile, runRecordDto).collect {
            if(it.success){
                emit(Result.Success(it))
            }
        }
    }.catch { e ->
        emit(Result.Error(e))
    }
}