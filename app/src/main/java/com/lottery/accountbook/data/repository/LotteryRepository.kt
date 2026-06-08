package com.lottery.accountbook.data.repository

import com.lottery.accountbook.data.dao.BudgetSettingDao
import com.lottery.accountbook.data.dao.LotteryRecordDao
import com.lottery.accountbook.data.dao.LotteryTypeDao
import com.lottery.accountbook.data.entity.BudgetSetting
import com.lottery.accountbook.data.entity.LotteryRecord
import com.lottery.accountbook.data.entity.LotteryType
import kotlinx.coroutines.flow.Flow

class LotteryRepository(
    private val recordDao: LotteryRecordDao,
    private val budgetDao: BudgetSettingDao,
    private val typeDao: LotteryTypeDao
) {
    val allRecords: Flow<List<LotteryRecord>> = recordDao.getAllRecords()
    val budgetSetting: Flow<BudgetSetting?> = budgetDao.getBudgetSetting()
    val lotteryTypes: Flow<List<LotteryType>> = typeDao.getAllTypes()

    fun getRecordsBetween(start: Long, end: Long): Flow<List<LotteryRecord>> =
        recordDao.getRecordsBetween(start, end)

    suspend fun insertRecord(record: LotteryRecord) = recordDao.insert(record)
    suspend fun updateRecord(record: LotteryRecord) = recordDao.update(record)
    suspend fun deleteRecord(record: LotteryRecord) = recordDao.delete(record)

    suspend fun saveBudget(setting: BudgetSetting) = budgetDao.insertOrUpdate(setting)

    suspend fun insertLotteryType(type: LotteryType) = typeDao.insert(type)
    suspend fun deleteLotteryType(type: LotteryType) = typeDao.delete(type)
}