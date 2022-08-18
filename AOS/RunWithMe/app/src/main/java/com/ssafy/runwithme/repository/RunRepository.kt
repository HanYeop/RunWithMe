package com.ssafy.runwithme.repository

import android.util.Log
import com.ssafy.runwithme.datasource.local.RunLocalDataSource
import com.ssafy.runwithme.model.entity.RunRecordEntity
import com.ssafy.runwithme.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
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
    }.catch { e ->
        emit(Result.Error(e))
    }

    fun deleteRun(run: RunRecordEntity) = runLocalDataSource.deleteRun(run)

    fun getTotalTimeInMillis(): Flow<Result<Int>> = flow {
        emit(Result.Loading)
        runLocalDataSource.getTotalTimeInMillis().collect {
            emit(Result.Success(it))
        }
    }.catch { e ->
        emit(Result.Error(e))
    }

    fun getTotalDistance(): Flow<Result<Int>> = flow {
        emit(Result.Loading)
        runLocalDataSource.getTotalDistance().collect {
            emit(Result.Success(it))
        }
    }.catch { e ->
        emit(Result.Error(e))
    }

    fun getTotalAvgSpeed(): Flow<Result<Double>> = flow {
        emit(Result.Loading)
        runLocalDataSource.getTotalAvgSpeed().collect {
            Log.d("test5", "getTotalAvgSpeed: $it")
            emit(Result.Success(it))
        }
    }.catch { e ->
        Log.d("test5", "getTotalAvgSpeed: $e")
        emit(Result.Error(e))
    }

    fun getTotalCaloriesBurned(): Flow<Result<Int>> = flow {
        emit(Result.Loading)
        runLocalDataSource.getTotalCaloriesBurned().collect {
            emit(Result.Success(it))
        }
    }.catch { e ->
        emit(Result.Error(e))
    }
}