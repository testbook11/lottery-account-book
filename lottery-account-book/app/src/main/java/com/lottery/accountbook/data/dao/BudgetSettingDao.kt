package com.lottery.accountbook.data.dao

import androidx.room.*
import com.lottery.accountbook.data.entity.BudgetSetting
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetSettingDao {
    @Query("SELECT * FROM budget_setting WHERE id = 1")
    fun getBudgetSetting(): Flow<BudgetSetting?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(setting: BudgetSetting)
}