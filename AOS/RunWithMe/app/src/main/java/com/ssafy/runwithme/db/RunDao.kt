package com.ssafy.runwithme.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ssafy.runwithme.model.entity.RunRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RunDao {

    // 기록 추가
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRun(run: RunRecordEntity)

    // 날짜 순으로 정렬
    @Query("SELECT * FROM run_table ORDER BY seq DESC")
    fun getAllRunsSortedByDate(): Flow<List<RunRecordEntity>>

    // 기록 삭제
    @Delete
    fun deleteRun(run: RunRecordEntity)

    // 시간 합계
    @Query("SELECT SUM(runningTime) FROM run_table")
    fun getTotalTimeInMillis(): Flow<Int>

    // 거리 합계
    @Query("SELECT SUM(runningDistance) FROM run_table")
    fun getTotalDistance(): Flow<Int>

    // 평균 속도
    @Query("SELECT AVG(avgSpeed) FROM run_table")
    fun getTotalAvgSpeed(): Flow<Double>

    // 칼로리 합계
    @Query("SELECT SUM(calorie) FROM run_table")
    fun getTotalCaloriesBurned(): Flow<Int>

}