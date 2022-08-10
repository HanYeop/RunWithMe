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
}