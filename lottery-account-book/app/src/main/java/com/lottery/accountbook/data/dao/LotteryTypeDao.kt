package com.lottery.accountbook.data.dao

import androidx.room.*
import com.lottery.accountbook.data.entity.LotteryType
import kotlinx.coroutines.flow.Flow

@Dao
interface LotteryTypeDao {
    @Query("SELECT * FROM lottery_types ORDER BY id ASC")
    fun getAllTypes(): Flow<List<LotteryType>>

    @Insert
    suspend fun insert(type: LotteryType)

    @Delete
    suspend fun delete(type: LotteryType)
}