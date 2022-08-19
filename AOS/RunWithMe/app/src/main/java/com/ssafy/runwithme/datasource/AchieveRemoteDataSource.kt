package com.ssafy.runwithme.datasource

import com.ssafy.runwithme.api.AchieveApi
import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.response.MyAchieveResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AchieveRemoteDataSource @Inject constructor(
    private val achieveApi: AchieveApi
) {

    fun getMyAchieve() : Flow<BaseResponse<List<MyAchieveResponse>>> = flow {
        emit(achieveApi.getMyAchieve())
    }

}