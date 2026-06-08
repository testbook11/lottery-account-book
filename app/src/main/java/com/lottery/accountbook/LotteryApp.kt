package com.lottery.accountbook

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.lottery.accountbook.data.database.AppDatabase
import com.lottery.accountbook.data.repository.LotteryRepository
import com.lottery.accountbook.ui.navigation.NavGraph

@Composable
fun LotteryApp() {
    val context = LocalContext.current
    val database = remember { AppDatabase.getDatabase(context) }
    val repository = remember {
        LotteryRepository(
            recordDao = database.lotteryRecordDao(),
            budgetDao = database.budgetSettingDao(),
            typeDao = database.lotteryTypeDao()
        )
    }
    val navController = rememberNavController()
    NavGraph(navController = navController)
    // 存储库可通过 CompositionLocal 传递，为简化在ViewModel中直接获取
    // 这里通过伴生对象提供全局访问，注意线程安全
    LotteryRepositoryHolder.repository = repository
}

object LotteryRepositoryHolder {
    lateinit var repository: LotteryRepository
}