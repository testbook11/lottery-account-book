package com.lottery.accountbook.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.lottery.accountbook.ui.screens.addrecord.AddRecordScreen
import com.lottery.accountbook.ui.screens.analysis.AnalysisScreen
import com.lottery.accountbook.ui.screens.budget.BudgetScreen
import com.lottery.accountbook.ui.screens.detail.DetailScreen
import com.lottery.accountbook.ui.screens.export.ExportScreen
import com.lottery.accountbook.ui.screens.home.HomeScreen
import com.lottery.accountbook.ui.screens.statistics.StatisticsScreen

object Routes {
    const val HOME = "home"
    const val ADD_RECORD = "add_record"
    const val DETAIL = "detail/{recordId}"
    const val STATISTICS = "statistics"
    const val ANALYSIS = "analysis"
    const val EXPORT = "export"
    const val BUDGET = "budget"

    fun detailRoute(recordId: Int) = "detail/$recordId"
}

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            HomeScreen(
                onAddRecord = { navController.navigate(Routes.ADD_RECORD) },
                onRecordClick = { id -> navController.navigate(Routes.detailRoute(id)) },
                onStatistics = { navController.navigate(Routes.STATISTICS) },
                onAnalysis = { navController.navigate(Routes.ANALYSIS) },
                onExport = { navController.navigate(Routes.EXPORT) },
                onBudget = { navController.navigate(Routes.BUDGET) }
            )
        }
        composable(Routes.ADD_RECORD) {
            AddRecordScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument("recordId") { type = NavType.IntType })
        ) { backStackEntry ->
            val recordId = backStackEntry.arguments?.getInt("recordId") ?: return@composable
            DetailScreen(
                recordId = recordId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Routes.STATISTICS) {
            StatisticsScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(Routes.ANALYSIS) {
            AnalysisScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(Routes.EXPORT) {
            ExportScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(Routes.BUDGET) {
            BudgetScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}