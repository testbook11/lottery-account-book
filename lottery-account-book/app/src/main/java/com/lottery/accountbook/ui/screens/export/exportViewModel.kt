package com.lottery.accountbook.ui.screens.export

import android.app.Application
import android.content.ContentValues
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.lottery.accountbook.LotteryRepositoryHolder
import com.lottery.accountbook.data.entity.LotteryRecord
import com.lottery.accountbook.util.ExcelExporter
import com.lottery.accountbook.util.PdfExporter
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

enum class ExportFormat { EXCEL, PDF }
enum class ExportContent { ALL_RECORDS, STATISTICS, ANALYSIS }

class ExportViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application
    private val repository = LotteryRepositoryHolder.repository
    private val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())

    private val _startDate = MutableStateFlow(getDefaultStartDate())
    private val _endDate = MutableStateFlow(getDefaultEndDate())
    val startDate: StateFlow<Long> = _startDate
    val endDate: StateFlow<Long> = _endDate

    fun setDateRange(start: Long, end: Long) {
        _startDate.value = start
        _endDate.value = end
    }

    fun export(
        format: ExportFormat,
        content: ExportContent,
        onResult: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            val records = repository.getRecordsBetween(
                startDate.value,
                endDate.value + 86400000 - 1
            ).first()

            if (records.isEmpty()) {
                onResult(false, "暂无数据可导出")
                return@launch
            }

            val fileName = "彩票记账本_${dateFormat.format(Date(startDate.value))}_${dateFormat.format(Date(endDate.value))}"
            val result = when (format) {
                ExportFormat.EXCEL -> {
                    val file = ExcelExporter.exportRecords(context, records, fileName)
                    file != null
                }
                ExportFormat.PDF -> {
                    val file = PdfExporter.exportSummary(context, records, fileName)
                    file != null
                }
            }
            if (result) {
                onResult(true, "文件已保存至 Downloads/彩票记账本 目录")
            } else {
                onResult(false, "导出失败，请检查存储权限或空间")
            }
        }
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