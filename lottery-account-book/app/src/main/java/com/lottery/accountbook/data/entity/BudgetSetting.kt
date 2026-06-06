package com.lottery.accountbook.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budget_setting")
data class BudgetSetting(
    @PrimaryKey val id: Int = 1,
    val monthlyBudget: Double = 0.0,
    val alertThreshold: Double = 0.8, // Ô¤¾¯±ÈÀý£¬Ä¬ÈÏ80%
    val alertEnabled: Boolean = true
)