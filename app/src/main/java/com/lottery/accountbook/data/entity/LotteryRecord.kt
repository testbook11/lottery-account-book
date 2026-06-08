package com.lottery.accountbook.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lottery_records")
data class LotteryRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Long,               // 购买日期时间戳（毫秒）
    val lotteryType: String,      // 彩票类型名称
    val betAmount: Double,        // 投注金额
    val numbers: String? = null,  // 投注号码
    val reason: String? = null,   // 购彩理由
    val winAmount: Double = 0.0,  // 中奖金额
    val note: String? = null      // 备注
)