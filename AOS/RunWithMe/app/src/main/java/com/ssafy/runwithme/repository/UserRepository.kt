package com.ssafy.runwithme.repository

import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.datasource.UserRemoteDataSource
import com.ssafy.runwithme.model.dto.FcmTokenDto
import com.ssafy.runwithme.model.dto.UserDto
import com.ssafy.runwithme.model.response.JoinResponse
import com.ssafy.runwithme.model.response.OtherUserFileDto
import com.ssafy.runwithme.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource
){

    fun joinUser(token: String, userDto: UserDto): Flow<Result<BaseResponse<JoinResponse>>> = flow {
        emit(Result.Loading)
        userRemoteDataSource.joinUser(token, userDto).collect {
            if(it.success){
                emit(Result.Success(it))
            }else if(!it.success){
                emit(Result.Fail(it))
            }else{
                emit(Result.Empty)
            }
        }
    }.catch { e ->
        emit(Result.Error(e))
    }

    fun fcmToken(fcmTokenDto: FcmTokenDto): Flow<Result<BaseResponse<String>>> = flow {
        emit(Result.Loading)
        userRemoteDataSource.fcmToken(fcmTokenDto).collect {
            if(it.success){
                emit(Result.Success(it))
            }else if(!it.success){
                emit(Result.Fail(it))
            }else{
                emit(Result.Empty)
            }
        }
    }.catch { e ->
        emit(Result.Error(e))
    }

    fun deleteFcmToken(): Flow<Result<BaseResponse<String>>> = flow {
        emit(Result.Loading)
        userRemoteDataSource.deleteFcmToken().collect {
            if(it.success){
                emit(Result.Success(it))
            }else if(!it.success){
                emit(Result.Fail(it))
            }else{
                emit(Result.Empty)
            }
        }
    }.catch { e ->
        emit(Result.Error(e))
    }

    fun getUserProfile(userSeq: Int): Flow<Result<BaseResponse<OtherUserFileDto>>> = flow {
        emit(Result.Loading)
        userRemoteDataSource.getUserProfile(userSeq).collect {
            if(it.success){
                emit(Result.Success(it))
            }else if(!it.success){
                emit(Result.Fail(it))
            }else{
                emit(Result.Empty)
            }
        }
    }.catch { e ->
        emit(Result.Error(e))
    }

}