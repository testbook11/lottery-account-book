package com.lottery.accountbook.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lottery_types")
data class LotteryType(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val isPreset: Boolean = false
)