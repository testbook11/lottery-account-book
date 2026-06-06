package com.lottery.accountbook.ui.screens.budget

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lottery.accountbook.ui.components.PaperBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetScreen(
    onNavigateBack: () -> Unit,
    viewModel: BudgetViewModel = viewModel()
) {
    val context = LocalContext.current
    val budgetSetting by viewModel.budgetSetting

    var budgetText by remember { mutableStateOf("") }
    var thresholdText by remember { mutableStateOf("") }
    var alertEnabled by remember { mutableStateOf(true) }

    LaunchedEffect(budgetSetting) {
        budgetSetting?.let {
            budgetText = it.monthlyBudget.takeIf { d -> d > 0 }?.let { "%.0f".format(it) } ?: ""
            thresholdText = "${(it.alertThreshold * 100).toInt()}"
            alertEnabled = it.alertEnabled
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("预算设置") },
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
            OutlinedTextField(
                value = budgetText,
                onValueChange = { budgetText = it },
                label = { Text("月度购彩预算（元）") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = thresholdText,
                onValueChange = { thresholdText = it },
                label = { Text("预警阈值（%）") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                supportingText = { Text("达到预算的此百分比时提醒，默认80%") }
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("开启预算提醒", style = MaterialTheme.typography.bodyLarge)
                Switch(checked = alertEnabled, onCheckedChange = { alertEnabled = it })
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    val budget = budgetText.toDoubleOrNull()
                    val threshold = thresholdText.toIntOrNull()
                    if (budget == null || budget <= 0) {
                        Toast.makeText(context, "请输入有效的预算金额", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (threshold == null || threshold !in 1..100) {
                        Toast.makeText(context, "预警阈值需在1-100之间", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    viewModel.saveBudget(budget, threshold / 100.0, alertEnabled)
                    Toast.makeText(context, "预算设置已保存", Toast.LENGTH_SHORT).show()
                    onNavigateBack()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("保存")
            }
        }
    }
}