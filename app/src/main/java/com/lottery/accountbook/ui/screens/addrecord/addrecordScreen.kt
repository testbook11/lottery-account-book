package com.lottery.accountbook.ui.screens.addrecord

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lottery.accountbook.ui.components.PaperBackground
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecordScreen(
    onNavigateBack: () -> Unit,
    viewModel: AddRecordViewModel = viewModel()
) {
    val context = LocalContext.current
    val lotteryTypes by viewModel.lotteryTypes

    var selectedDate by remember { mutableStateOf(System.currentTimeMillis()) }
    var selectedType by remember { mutableStateOf("") }
    var typeDropdownExpanded by remember { mutableStateOf(false) }
    var betAmount by remember { mutableStateOf("") }
    var numbers by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }
    var winAmount by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    var showAddTypeDialog by remember { mutableStateOf(false) }
    var newTypeName by remember { mutableStateOf("") }

    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    // 预置彩票类型（若数据库为空则预先插入）
    LaunchedEffect(Unit) {
        if (lotteryTypes.isEmpty()) {
            val presets = listOf("双色球", "大乐透", "福彩3D", "排列三", "排列五", "七乐彩", "快乐8")
            presets.forEach { viewModel.addCustomType(it) }
        }
    }

    // 日期选择器
    if (showDatePicker) {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val cal = Calendar.getInstance()
                cal.set(year, month, dayOfMonth)
                selectedDate = cal.timeInMillis
                showDatePicker = false
            },
            Calendar.getInstance().apply { timeInMillis = selectedDate }.get(Calendar.YEAR),
            Calendar.getInstance().apply { timeInMillis = selectedDate }.get(Calendar.MONTH),
            Calendar.getInstance().apply { timeInMillis = selectedDate }.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    // 监听保存成功
    LaunchedEffect(Unit) {
        viewModel.saveSuccess.collect { success ->
            if (success) {
                Toast.makeText(context, "记录已保存", Toast.LENGTH_SHORT).show()
                onNavigateBack()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("添加记录") },
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 购买日期
            OutlinedTextField(
                value = dateFormat.format(Date(selectedDate)),
                onValueChange = {},
                label = { Text("购买日期") },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = "选择日期")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            // 彩票类型
            ExposedDropdownMenuBox(
                expanded = typeDropdownExpanded,
                onExpandedChange = { typeDropdownExpanded = it }
            ) {
                OutlinedTextField(
                    value = selectedType,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("彩票类型") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeDropdownExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = typeDropdownExpanded,
                    onDismissRequest = { typeDropdownExpanded = false }
                ) {
                    lotteryTypes.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type.name) },
                            onClick = {
                                selectedType = type.name
                                typeDropdownExpanded = false
                            }
                        )
                    }
                    DropdownMenuItem(
                        text = { Text("+ 自定义类型") },
                        onClick = {
                            typeDropdownExpanded = false
                            showAddTypeDialog = true
                        }
                    )
                }
            }

            // 投注金额
            OutlinedTextField(
                value = betAmount,
                onValueChange = { betAmount = it },
                label = { Text("投注金额") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                isError = betAmount.toDoubleOrNull()?.let { it <= 0 } ?: false,
                supportingText = {
                    if (betAmount.toDoubleOrNull()?.let { it <= 0 } == true)
                        Text("请输入有效金额")
                }
            )

            // 投注号码（可选）
            OutlinedTextField(
                value = numbers,
                onValueChange = { numbers = it },
                label = { Text("投注号码（可选）") },
                modifier = Modifier.fillMaxWidth()
            )

            // 购彩理由（可选）
            OutlinedTextField(
                value = reason,
                onValueChange = { reason = it },
                label = { Text("购彩理由（可选）") },
                modifier = Modifier.fillMaxWidth()
            )

            // 中奖金额
            OutlinedTextField(
                value = winAmount,
                onValueChange = { winAmount = it },
                label = { Text("中奖金额（开奖后补充）") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                isError = winAmount.toDoubleOrNull()?.let { it < 0 } ?: false
            )

            // 备注
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("备注（可选）") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )

            Spacer(Modifier.height(16.dp))

            // 保存按钮
            Button(
                onClick = {
                    val bet = betAmount.toDoubleOrNull()
                    if (bet == null || bet <= 0) {
                        Toast.makeText(context, "请输入有效投注金额", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (selectedType.isBlank()) {
                        Toast.makeText(context, "请选择彩票类型", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    val win = winAmount.toDoubleOrNull() ?: 0.0
                    if (win < 0) {
                        Toast.makeText(context, "中奖金额不能为负数", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    viewModel.saveRecord(
                        date = selectedDate,
                        lotteryType = selectedType,
                        betAmount = bet,
                        numbers = numbers,
                        reason = reason,
                        winAmount = win,
                        note = note
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("保存")
            }
        }

        // 自定义类型对话框
        if (showAddTypeDialog) {
            AlertDialog(
                onDismissRequest = { showAddTypeDialog = false },
                title = { Text("添加自定义彩票类型") },
                text = {
                    OutlinedTextField(
                        value = newTypeName,
                        onValueChange = { newTypeName = it },
                        label = { Text("类型名称") },
                        singleLine = true
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        if (newTypeName.isNotBlank()) {
                            viewModel.addCustomType(newTypeName)
                            selectedType = newTypeName
                            newTypeName = ""
                            showAddTypeDialog = false
                        }
                    }) { Text("确定") }
                },
                dismissButton = {
                    TextButton(onClick = { showAddTypeDialog = false }) { Text("取消") }
                }
            )
        }
    }
}