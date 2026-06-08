package com.lottery.accountbook.ui.screens.budget

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.lottery.accountbook.LotteryRepositoryHolder
import com.lottery.accountbook.data.entity.BudgetSetting
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class BudgetViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = LotteryRepositoryHolder.repository

    val budgetSetting: StateFlow<BudgetSetting?> = repository.budgetSetting
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun saveBudget(monthlyBudget: Double, alertThreshold: Double, alertEnabled: Boolean) {
        viewModelScope.launch {
            repository.saveBudget(
                BudgetSetting(
                    monthlyBudget = monthlyBudget,
                    alertThreshold = alertThreshold,
                    alertEnabled = alertEnabled
                )
            )
        }
    }
}