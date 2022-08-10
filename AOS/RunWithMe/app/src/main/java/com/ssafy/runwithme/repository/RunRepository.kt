package com.ssafy.runwithme.repository

import com.ssafy.runwithme.datasource.local.RunLocalDataSource
import com.ssafy.runwithme.model.entity.RunRecordEntity
import com.ssafy.runwithme.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RunRepository @Inject constructor(
    private val runLocalDataSource: RunLocalDataSource
){
    fun insertRun(run: RunRecordEntity) = runLocalDataSource.insertRun(run)
    fun getAllRunsSortedByDate(): Flow<Result<List<RunRecordEntity>>> = flow {
        emit(Result.Loading)
        runLocalDataSource.getAllRunsSortedByDate().collect {
            if(it.isEmpty()){
                emit(Result.Empty)
            }else{
                emit(Result.Success(it))
            }
        }
    }
}