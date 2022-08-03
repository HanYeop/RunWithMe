package com.ssafy.runwithme.repository

import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.datasource.AchieveRemoteDataSource
import com.ssafy.runwithme.model.response.MyAchieveResponse
import com.ssafy.runwithme.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AchieveRepository @Inject constructor(
    private val achieveRemoteDataSource: AchieveRemoteDataSource
) {

    fun getMyAchieve() : Flow<Result<BaseResponse<List<MyAchieveResponse>>>> = flow {
        emit(Result.Loading)
        achieveRemoteDataSource.getMyAchieve().collect {
            if(it.success){
                emit(Result.Success(it))
            } else {
                emit(Result.Empty)
            }
        }
    }.catch { e ->
        emit(Result.Error(e))
    }

}