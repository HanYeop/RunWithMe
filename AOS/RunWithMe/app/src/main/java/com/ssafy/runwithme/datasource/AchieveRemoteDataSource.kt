package com.ssafy.runwithme.datasource

import android.util.Log
import com.ssafy.runwithme.api.AchieveApi
import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.response.MyAchieveResponse
import com.ssafy.runwithme.utils.TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AchieveRemoteDataSource @Inject constructor(
    private val achieveApi: AchieveApi
) {

    fun getMyAchieve() : Flow<BaseResponse<List<MyAchieveResponse>>> = flow {
        emit(achieveApi.getMyAchieve())
    }

}