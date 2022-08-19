package com.ssafy.runwithme.repository

import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.datasource.CustomerCenterRemoteDataSource
import com.ssafy.runwithme.model.dto.ReportDto
import com.ssafy.runwithme.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomerCenterRepository @Inject constructor(
    private val customerCenterRemoteDataSource: CustomerCenterRemoteDataSource
) {

    fun reportBoard(boardSeq: Int, content: String) : Flow<Result<BaseResponse<ReportDto>>>  = flow {
        emit(Result.Loading)
        customerCenterRemoteDataSource.reportBoard(boardSeq, content).collect {
            if(it.success){
                emit(Result.Success(it))
            } else if (!it.success){
                emit(Result.Fail(it))
            } else {
                emit(Result.Empty)
            }
        }
    }.catch { e ->
        emit(Result.Error(e))
    }

}