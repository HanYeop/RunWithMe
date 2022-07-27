package com.ssafy.runwithme.repository

import android.util.Log
import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.datasource.MyActivityRemoteDataSource
import com.ssafy.runwithme.model.dto.UserDto
import com.ssafy.runwithme.model.response.MyProfileResponse
import com.ssafy.runwithme.utils.Result
import com.ssafy.runwithme.utils.TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MyActivityRepository @Inject constructor(
    private val myActivityRemoteDataSource: MyActivityRemoteDataSource
){
    fun getMyProfile(): Flow<Result<BaseResponse<MyProfileResponse>>> = flow {
        emit(Result.Loading)
        myActivityRemoteDataSource.getMyProfile().collect {
            if(it.success){
                emit(Result.Success(it))
            }else {
                emit(Result.Empty)
            }
        }
    }.catch { e ->
        emit(Result.Error(e))
    }

    fun editMyProfile(userDto: UserDto): Flow<Result<BaseResponse<MyProfileResponse>>> = flow {
        emit(Result.Loading)
        myActivityRemoteDataSource.editMyProfile(userDto).collect {
            if(it.success){
                emit(Result.Success(it))
            }else {
                emit(Result.Empty)
            }
        }
    }.catch { e ->
        emit(Result.Error(e))
    }
}