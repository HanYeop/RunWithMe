package com.ssafy.runwithme.datasource

import com.ssafy.runwithme.api.ScrapApi
import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.dto.ScrapInfoDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ScrapRemoteDataSource @Inject constructor(
    private val scrapApi: ScrapApi
){
    fun addMyScrap(trackBoardSeq : Int, title : String): Flow<BaseResponse<ScrapInfoDto>> = flow {
        emit(scrapApi.addMyScrap(trackBoardSeq, title))
    }

    fun getMyScrap(title : String): Flow<BaseResponse<List<ScrapInfoDto>>> = flow {
        emit(scrapApi.getMyScrap(title))
    }

    fun deleteMyScrap(scrapSeq : Int) : Flow<BaseResponse<Boolean>> = flow {
        emit(scrapApi.deleteMyScrap(scrapSeq))
    }
}