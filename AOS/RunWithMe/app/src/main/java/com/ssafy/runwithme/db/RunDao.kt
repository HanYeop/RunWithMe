package com.ssafy.runwithme.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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
}