package com.ssafy.runwithme.repository

import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.datasource.ScrapRemoteDataSource
import com.ssafy.runwithme.model.dto.ScrapInfoDto
import com.ssafy.runwithme.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScrapRepository @Inject constructor(
    private val scrapRemoteDataSource : ScrapRemoteDataSource
){
    fun addMyScrap(trackBoardSeq : Int, title : String): Flow<Result<BaseResponse<ScrapInfoDto>>> = flow {
        emit(Result.Loading)
        scrapRemoteDataSource.addMyScrap(trackBoardSeq, title).collect {
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

    fun getMyScrap(): Flow<Result<BaseResponse<List<ScrapInfoDto>>>> = flow {
        emit(Result.Loading)
        scrapRemoteDataSource.getMyScrap().collect {
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

    fun deleteMyScrap(scrapSeq : Int): Flow<Result<BaseResponse<Boolean>>> = flow {
        emit(Result.Loading)
        scrapRemoteDataSource.deleteMyScrap(scrapSeq).collect {
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