package com.ssafy.runwithme.datasource

import com.ssafy.runwithme.api.MyActivityApi
import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.response.MyProfileResponse
import com.ssafy.runwithme.model.response.MyTotalRecordResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class MyActivityRemoteDataSource @Inject constructor(
    private val myActivityApi: MyActivityApi
){
    fun getMyProfile(): Flow<BaseResponse<MyProfileResponse>> = flow {
        emit(myActivityApi.getMyProfile())
    }

    fun editMyProfile(profileEditDto: RequestBody, imgFile : MultipartBody.Part?
    ): Flow<BaseResponse<MyProfileResponse>> = flow {
        emit(myActivityApi.editMyProfile(profileEditDto, imgFile))
    }

    fun getMyTotalRecord(): Flow<BaseResponse<MyTotalRecordResponse>> = flow {
        emit(myActivityApi.getMyTotalRecord())
    }
}