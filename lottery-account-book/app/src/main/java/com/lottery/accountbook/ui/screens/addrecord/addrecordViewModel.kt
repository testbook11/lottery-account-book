package com.lottery.accountbook.ui.screens.addrecord

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.lottery.accountbook.LotteryRepositoryHolder
import com.lottery.accountbook.data.entity.LotteryRecord
import com.lottery.accountbook.data.entity.LotteryType
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class AddRecordViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = LotteryRepositoryHolder.repository

    val lotteryTypes: StateFlow<List<LotteryType>> = repository.lotteryTypes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _saveSuccess = MutableSharedFlow<Boolean>()
    val saveSuccess: SharedFlow<Boolean> = _saveSuccess

    fun saveRecord(
        date: Long,
        lotteryType: String,
        betAmount: Double,
        numbers: String?,
        reason: String?,
        winAmount: Double,
        note: String?
    ) {
        viewModelScope.launch {
            val record = LotteryRecord(
                date = date,
                lotteryType = lotteryType,
                betAmount = betAmount,
                numbers = numbers?.takeIf { it.isNotBlank() },
                reason = reason?.takeIf { it.isNotBlank() },
                winAmount = winAmount,
                note = note?.takeIf { it.isNotBlank() }
            )
            repository.insertRecord(record)
            _saveSuccess.emit(true)
        }
    }

    fun addCustomType(name: String) {
        viewModelScope.launch {
            repository.insertLotteryType(LotteryType(name = name, isPreset = false))
        }
    }
}