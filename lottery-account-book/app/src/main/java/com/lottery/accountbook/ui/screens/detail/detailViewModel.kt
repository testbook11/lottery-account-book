package com.lottery.accountbook.ui.screens.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.lottery.accountbook.LotteryRepositoryHolder
import com.lottery.accountbook.data.entity.LotteryRecord
import com.lottery.accountbook.data.entity.LotteryType
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = LotteryRepositoryHolder.repository

    private val _record = MutableStateFlow<LotteryRecord?>(null)
    val record: StateFlow<LotteryRecord?> = _record

    val lotteryTypes: StateFlow<List<LotteryType>> = repository.lotteryTypes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun loadRecord(recordId: Int) {
        viewModelScope.launch {
            repository.allRecords.collect { records ->
                _record.value = records.find { it.id == recordId }
            }
        }
    }

    fun updateRecord(record: LotteryRecord) {
        viewModelScope.launch {
            repository.updateRecord(record)
        }
    }

    fun deleteRecord(record: LotteryRecord) {
        viewModelScope.launch {
            repository.deleteRecord(record)
        }
    }
}