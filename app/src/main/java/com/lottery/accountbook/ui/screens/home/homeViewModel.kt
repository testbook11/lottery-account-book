package com.lottery.accountbook.ui.screens.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.lottery.accountbook.LotteryRepositoryHolder
import com.lottery.accountbook.data.entity.BudgetSetting
import com.lottery.accountbook.data.entity.LotteryRecord
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = LotteryRepositoryHolder.repository

    val records: StateFlow<List<LotteryRecord>> = repository.allRecords
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val budgetSetting: StateFlow<BudgetSetting?> = repository.budgetSetting
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // 当前月累计投注金额
    private val _currentMonthTotal = MutableStateFlow(0.0)
    val currentMonthTotal: StateFlow<Double> = _currentMonthTotal

    // 是否显示预警卡片
    val showWarning = combine(records, budgetSetting) { records, setting ->
        val total = calculateCurrentMonthTotal(records)
        _currentMonthTotal.value = total
        if (setting != null && setting.alertEnabled && setting.monthlyBudget > 0) {
            total >= setting.monthlyBudget * setting.alertThreshold
        } else false
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    private fun calculateCurrentMonthTotal(records: List<LotteryRecord>): Double {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val startOfMonth = Calendar.getInstance().apply {
            set(year, month, 1, 0, 0, 0)
        }.timeInMillis
        val endOfMonth = Calendar.getInstance().apply {
            set(year, month + 1, 1, 0, 0, 0)
        }.timeInMillis - 1
        return records.filter { it.date in startOfMonth..endOfMonth }
            .sumOf { it.betAmount }
    }

    fun deleteRecord(record: LotteryRecord) {
        viewModelScope.launch {
            repository.deleteRecord(record)
        }
    }
}