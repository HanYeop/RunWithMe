package com.ssafy.runwithme.repository

import android.util.Log
import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.datasource.CrewManagerRemoteDataSource
import com.ssafy.runwithme.model.response.MyCurrentCrewResponse
import com.ssafy.runwithme.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CrewManagerRepository @Inject constructor(
    private val crewManagerRemoteDataSource: CrewManagerRemoteDataSource
){
    fun getMyCurrentCrew(): Flow<Result<BaseResponse<List<MyCurrentCrewResponse>>>> = flow {
        emit(Result.Loading)
        crewManagerRemoteDataSource.getMyCurrentCrew().collect {
            if(it.data.isEmpty()){
                emit(Result.Empty)
            }else if(it.success){
                emit(Result.Success(it))
            }
        }
    }.catch { e ->
        emit(Result.Error(e))
    }
}