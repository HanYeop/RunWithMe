package com.ssafy.runwithme.repository

import com.ssafy.runwithme.datasource.CrewManagerRemoteDataSource
import com.ssafy.runwithme.model.dto.MyCurrentCrewResponse
import com.ssafy.runwithme.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CrewManagerRepository @Inject constructor(
    private val crewManagerRemoteDataSource: CrewManagerRemoteDataSource
){
    fun getMyCurrentCrew(): Flow<Result<List<MyCurrentCrewResponse>>> = flow {
        emit(Result.Loading)
        crewManagerRemoteDataSource.getMyCurrentCrew().collect {
            if(it.isEmpty()){
                emit(Result.Empty)
            }else{
                emit(Result.Success(it))
            }
        }
    }.catch { e ->
        emit(Result.Error(e))
    }
}