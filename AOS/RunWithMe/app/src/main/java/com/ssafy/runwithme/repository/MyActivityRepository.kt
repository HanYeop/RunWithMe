package com.ssafy.runwithme.repository

import android.util.Log
import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.datasource.MyActivityRemoteDataSource
import com.ssafy.runwithme.model.dto.ImageFileDto
import com.ssafy.runwithme.model.dto.UserDto
import com.ssafy.runwithme.model.response.MyProfileResponse
import com.ssafy.runwithme.utils.Result
import com.ssafy.runwithme.utils.TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    fun editMyProfile(profileEditDto: RequestBody, imgFile : MultipartBody.Part?
    ): Flow<Result<BaseResponse<MyProfileResponse>>> = flow {
        emit(Result.Loading)
        myActivityRemoteDataSource.editMyProfile(profileEditDto, imgFile).collect {
            Log.d(TAG, "받아온 결과 : $it")
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