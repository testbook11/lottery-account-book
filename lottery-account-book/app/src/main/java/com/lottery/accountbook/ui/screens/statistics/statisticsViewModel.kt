package com.lottery.accountbook.ui.screens.statistics

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.lottery.accountbook.LotteryRepositoryHolder
import com.lottery.accountbook.data.entity.LotteryRecord
import kotlinx.coroutines.flow.*
import java.text.SimpleDateFormat
import java.util.*

data class StatisticsData(
    val totalCount: Int = 0,
    val totalBet: Double = 0.0,
    val totalWin: Double = 0.0,
    val winCount: Int = 0,
    val winRate: Double = 0.0,
    val typeStats: Map<String, TypeStat> = emptyMap(),
    val dailyTrend: List<Pair<String, Int>> = emptyList() // 购彩次数趋势
)

data class TypeStat(
    val count: Int,
    val totalBet: Double,
    val totalWin: Double
)

class StatisticsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = LotteryRepositoryHolder.repository
    private val dateFormat = SimpleDateFormat("MM-dd", Locale.getDefault())

    private val _startDate = MutableStateFlow(getDefaultStartDate())
    private val _endDate = MutableStateFlow(getDefaultEndDate())

    val startDate: StateFlow<Long> = _startDate
    val endDate: StateFlow<Long> = _endDate

    val statistics: StateFlow<StatisticsData> = combine(
        repository.getRecordsBetween(
            startDate.value,
            endDate.value + 86400000 - 1 // 包含结束日期当天
        ),
        startDate,
        endDate
    ) { records, _, _ ->
        calculateStatistics(records)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StatisticsData())

    fun setDateRange(start: Long, end: Long) {
        _startDate.value = start
        _endDate.value = end
    }

    private fun calculateStatistics(records: List<LotteryRecord>): StatisticsData {
        val totalCount = records.size
        val totalBet = records.sumOf { it.betAmount }
        val totalWin = records.sumOf { it.winAmount }
        val winCount = records.count { it.winAmount > 0 }
        val winRate = if (totalCount > 0) winCount.toDouble() / totalCount else 0.0

        // 按类型分组
        val typeStats = records.groupBy { it.lotteryType }
            .mapValues { (_, list) ->
                TypeStat(
                    count = list.size,
                    totalBet = list.sumOf { it.betAmount },
                    totalWin = list.sumOf { it.winAmount }
                )
            }

        // 每日购彩次数趋势（按日期分组计数，然后按日期排序）
        val dailyTrend = records.groupBy {
            dateFormat.format(Date(it.date))
        }.mapValues { it.value.size }
            .toList()
            .sortedBy { it.first }

        return StatisticsData(
            totalCount = totalCount,
            totalBet = totalBet,
            totalWin = totalWin,
            winCount = winCount,
            winRate = winRate,
            typeStats = typeStats,
            dailyTrend = dailyTrend
        )
    }

    private fun getDefaultStartDate(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    private fun getDefaultEndDate(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        cal.set(Calendar.MILLISECOND, 999)
        return cal.timeInMillis
    }
}