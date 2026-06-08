package com.lottery.accountbook.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lottery.accountbook.data.entity.LotteryRecord
import com.lottery.accountbook.ui.components.BudgetWarningCard
import com.lottery.accountbook.ui.components.EmptyDataMessage
import com.lottery.accountbook.ui.components.PaperBackground
import com.lottery.accountbook.ui.theme.WarningOrange
import com.lottery.accountbook.ui.theme.WarningRed
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onAddRecord: () -> Unit,
    onRecordClick: (Int) -> Unit,
    onStatistics: () -> Unit,
    onAnalysis: () -> Unit,
    onExport: () -> Unit,
    onBudget: () -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val records by viewModel.records
    val showWarning by viewModel.showWarning
    val currentMonthTotal by viewModel.currentMonthTotal
    val budgetSetting by viewModel.budgetSetting

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("记录列表", style = MaterialTheme.typography.headlineLarge) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddRecord,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "添加记录")
            }
        }
    ) { padding ->
        PaperBackground(modifier = Modifier.padding(padding))
        Column(modifier = Modifier.fillMaxSize()) {
            // 预算预警卡片
            if (showWarning && budgetSetting != null) {
                BudgetWarningCard(
                    currentAmount = currentMonthTotal,
                    budget = budgetSetting!!.monthlyBudget,
                    threshold = budgetSetting!!.alertThreshold
                )
            }

            // 功能入口按钮栏
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SmallButton(Icons.Default.BarChart, "统计", onStatistics)
                SmallButton(Icons.Default.TrendingUp, "分析", onAnalysis)
                SmallButton(Icons.Default.FileDownload, "导出", onExport)
                SmallButton(Icons.Default.Settings, "预算", onBudget)
            }

            // 记录列表
            if (records.isEmpty()) {
                EmptyDataMessage()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(records, key = { it.id }) { record ->
                        RecordCard(record = record, onClick = { onRecordClick(record.id) })
                    }
                }
            }
        }
    }
}

@Composable
private fun SmallButton(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, onClick: () -> Unit) {
    OutlinedButton(onClick = onClick, contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(4.dp))
        Text(label, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
fun RecordCard(record: LotteryRecord, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(record.date)),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = record.lotteryType,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "投注: ￥${"%.2f".format(record.betAmount)}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            if (record.winAmount > 0) {
                Text(
                    text = "中奖: ￥${"%.2f".format(record.winAmount)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}