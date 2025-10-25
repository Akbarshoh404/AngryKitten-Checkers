package com.example.angrykitten_checkers.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.angrykitten_checkers.ui.BoardScreen
import com.example.angrykitten_checkers.ui.MainMenuScreen
import com.example.angrykitten_checkers.ui.StatsScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main_menu") {
        composable("main_menu") {
            MainMenuScreen(
                onTwoPlayersClick = { navController.navigate("board/two_players") },
                onVsAIClick = { navController.navigate("board/vs_ai") },
                onStatsClick = { navController.navigate("stats") }
            )
        }
        composable("board/{mode}") { backStackEntry ->
            val mode = backStackEntry.arguments?.getString("mode") ?: "two_players"
            BoardScreen(
                isVsAI = mode == "vs_ai",
                onBackClick = { navController.popBackStack() }
            )
        }
        composable("stats") {
            StatsScreen(onBackClick = { navController.popBackStack() })
        }
    }
}