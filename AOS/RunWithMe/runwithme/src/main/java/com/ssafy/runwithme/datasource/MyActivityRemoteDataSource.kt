package com.ssafy.runwithme.datasource

import android.util.Log
import com.ssafy.runwithme.api.MyActivityApi
import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.dto.RunRecordDto
import com.ssafy.runwithme.model.response.MyProfileResponse
import com.ssafy.runwithme.model.response.MyTotalRecordResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Path
import javax.inject.Inject

class MyActivityRemoteDataSource @Inject constructor(
    private val myActivityApi: MyActivityApi
){

    fun runAbleToday(crewSeq: Int): Flow<BaseResponse<Boolean>> = flow {
        emit(myActivityApi.runAbleToday(crewSeq))
    }

    fun getMyProfile(): Flow<BaseResponse<MyProfileResponse>> = flow {
        emit(myActivityApi.getMyProfile())
    }
}