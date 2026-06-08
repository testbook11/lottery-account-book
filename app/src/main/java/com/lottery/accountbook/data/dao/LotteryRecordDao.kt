package com.lottery.accountbook.data.dao

import androidx.room.*
import com.lottery.accountbook.data.entity.LotteryRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface LotteryRecordDao {
    @Query("SELECT * FROM lottery_records ORDER BY date DESC")
    fun getAllRecords(): Flow<List<LotteryRecord>>

    @Query("SELECT * FROM lottery_records WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getRecordsBetween(startDate: Long, endDate: Long): Flow<List<LotteryRecord>>

    @Insert
    suspend fun insert(record: LotteryRecord)

    @Update
    suspend fun update(record: LotteryRecord)

    @Delete
    suspend fun delete(record: LotteryRecord)

    @Query("DELETE FROM lottery_records")
    suspend fun deleteAll()
}