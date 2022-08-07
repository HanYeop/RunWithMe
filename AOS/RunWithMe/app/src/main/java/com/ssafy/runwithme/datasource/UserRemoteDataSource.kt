package com.ssafy.runwithme.datasource

import com.ssafy.runwithme.api.UserApi
import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.dto.FcmTokenDto
import com.ssafy.runwithme.model.dto.UserDto
import com.ssafy.runwithme.model.response.JoinResponse
import com.ssafy.runwithme.model.response.OtherUserFileDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.http.Body
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    private val userApi: UserApi
){
    fun joinUser(token: String, userDto: UserDto): Flow<BaseResponse<JoinResponse>> = flow {
        emit(userApi.joinUser(token, userDto))
    }

    fun fcmToken(fcmTokenDto: FcmTokenDto): Flow<BaseResponse<String>> = flow {
        emit(userApi.fcmToken(fcmTokenDto))
    }

    fun deleteFcmToken(): Flow<BaseResponse<String>> = flow {
        emit(userApi.deleteFcmToken())
    }

    fun getUserProfile(userSeq: Int): Flow<BaseResponse<OtherUserFileDto>> = flow {
        emit(userApi.getUserProfile(userSeq))
    }
}