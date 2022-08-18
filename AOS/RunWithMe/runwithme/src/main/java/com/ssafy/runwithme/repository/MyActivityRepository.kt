package com.ssafy.runwithme.repository

import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.datasource.MyActivityRemoteDataSource
import com.ssafy.runwithme.model.response.MyProfileResponse
import com.ssafy.runwithme.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MyActivityRepository @Inject constructor(
    private val myActivityRemoteDataSource: MyActivityRemoteDataSource
    ){


    fun runAbleToday(crewSeq: Int): Flow<Result<BaseResponse<Boolean>>> = flow {
        emit(Result.Loading)
        myActivityRemoteDataSource.runAbleToday(crewSeq).collect {
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

    fun getMyProfile(): Flow<Result<BaseResponse<MyProfileResponse>>> = flow {
        emit(Result.Loading)
        myActivityRemoteDataSource.getMyProfile().collect {
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