package com.lottery.accountbook.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lottery.accountbook.data.dao.BudgetSettingDao
import com.lottery.accountbook.data.dao.LotteryRecordDao
import com.lottery.accountbook.data.dao.LotteryTypeDao
import com.lottery.accountbook.data.entity.BudgetSetting
import com.lottery.accountbook.data.entity.LotteryRecord
import com.lottery.accountbook.data.entity.LotteryType

@Database(
    entities = [LotteryRecord::class, BudgetSetting::class, LotteryType::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun lotteryRecordDao(): LotteryRecordDao
    abstract fun budgetSettingDao(): BudgetSettingDao
    abstract fun lotteryTypeDao(): LotteryTypeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "lottery_account_book_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}