package com.ssafy.runwithme.datasource

import com.ssafy.runwithme.api.CustomerCenterApi
import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.dto.ReportDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CustomerCenterRemoteDataSource @Inject constructor(
    private val customerCenterApi: CustomerCenterApi
){
    fun reportBoard(boardSeq: Int, content: String): Flow<BaseResponse<ReportDto>> = flow {
        emit(customerCenterApi.reportBoard(boardSeq, content))
    }

}