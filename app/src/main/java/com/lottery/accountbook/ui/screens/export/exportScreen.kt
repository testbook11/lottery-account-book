package com.lottery.accountbook.ui.screens.export

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lottery.accountbook.ui.components.PaperBackground
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportScreen(
    onNavigateBack: () -> Unit,
    viewModel: ExportViewModel = viewModel()
) {
    val context = LocalContext.current
    var selectedFormat by remember { mutableStateOf(ExportFormat.EXCEL) }
    var selectedContent by remember { mutableStateOf(ExportContent.ALL_RECORDS) }
    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker by remember { mutableStateOf(false) }
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    if (showStartPicker) {
        DatePickerDialog(
            context,
            { _, year, month, day ->
                val cal = Calendar.getInstance()
                cal.set(year, month, day, 0, 0, 0)
                viewModel.setDateRange(cal.timeInMillis, viewModel.endDate.value)
                showStartPicker = false
            },
            Calendar.getInstance().apply { timeInMillis = viewModel.startDate.value }.get(Calendar.YEAR),
            Calendar.getInstance().apply { timeInMillis = viewModel.startDate.value }.get(Calendar.MONTH),
            Calendar.getInstance().apply { timeInMillis = viewModel.startDate.value }.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
    if (showEndPicker) {
        DatePickerDialog(
            context,
            { _, year, month, day ->
                val cal = Calendar.getInstance()
                cal.set(year, month, day, 23, 59, 59)
                if (cal.timeInMillis < viewModel.startDate.value) return@DatePickerDialog
                viewModel.setDateRange(viewModel.startDate.value, cal.timeInMillis)
                showEndPicker = false
            },
            Calendar.getInstance().apply { timeInMillis = viewModel.endDate.value }.get(Calendar.YEAR),
            Calendar.getInstance().apply { timeInMillis = viewModel.endDate.value }.get(Calendar.MONTH),
            Calendar.getInstance().apply { timeInMillis = viewModel.endDate.value }.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("数据导出") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        PaperBackground(modifier = Modifier.padding(padding))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 导出格式
            Text("选择导出格式", style = MaterialTheme.typography.titleLarge)
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                FilterChip(
                    selected = selectedFormat == ExportFormat.EXCEL,
                    onClick = { selectedFormat = ExportFormat.EXCEL },
                    label = { Text("Excel") }
                )
                FilterChip(
                    selected = selectedFormat == ExportFormat.PDF,
                    onClick = { selectedFormat = ExportFormat.PDF },
                    label = { Text("PDF") }
                )
            }

            // 导出内容
            Text("选择导出内容", style = MaterialTheme.typography.titleLarge)
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                FilterChip(
                    selected = selectedContent == ExportContent.ALL_RECORDS,
                    onClick = { selectedContent = ExportContent.ALL_RECORDS },
                    label = { Text("全部记录") }
                )
                FilterChip(
                    selected = selectedContent == ExportContent.STATISTICS,
                    onClick = { selectedContent = ExportContent.STATISTICS },
                    label = { Text("数据统计") }
                )
                FilterChip(
                    selected = selectedContent == ExportContent.ANALYSIS,
                    onClick = { selectedContent = ExportContent.ANALYSIS },
                    label = { Text("盈亏分析") }
                )
            }

            // 日期范围
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(onClick = { showStartPicker = true }) {
                    Icon(Icons.Default.CalendarMonth, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("起始: ${dateFormat.format(Date(viewModel.startDate.value))}")
                }
                OutlinedButton(onClick = { showEndPicker = true }) {
                    Icon(Icons.Default.CalendarMonth, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("结束: ${dateFormat.format(Date(viewModel.endDate.value))}")
                }
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.export(selectedFormat, selectedContent) { success, message ->
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                        if (success) onNavigateBack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("导出")
            }
        }
    }
}