package com.ssafy.runwithme.datasource.local

import com.ssafy.runwithme.db.RunDao
import com.ssafy.runwithme.model.entity.RunRecordEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RunLocalDataSource @Inject constructor(private val runDao: RunDao){
    fun insertRun(run: RunRecordEntity) = runDao.insertRun(run)
    fun getAllRunsSortedByDate(): Flow<List<RunRecordEntity>> = runDao.getAllRunsSortedByDate()
    fun deleteRun(run: RunRecordEntity) = runDao.deleteRun(run)
    fun getTotalTimeInMillis(): Flow<Int> = runDao.getTotalTimeInMillis()
    fun getTotalDistance(): Flow<Int> = runDao.getTotalDistance()
    fun getTotalAvgSpeed(): Flow<Double> = runDao.getTotalAvgSpeed()
    fun getTotalCaloriesBurned(): Flow<Int> = runDao.getTotalCaloriesBurned()
}