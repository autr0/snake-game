package com.example.snake_game.presentation.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.snake_game.presentation.SnakeViewModel

@Composable
fun Navigation(
    vm: SnakeViewModel
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                goAndPlay = { navController.navigate("game") },
                vm = vm,
                openSettings = { navController.navigate("settings") }
            )

        }

        composable("game") {
            GameScreen(
                { navController.navigateUp() },
                vm = vm
            )
        }

        composable("settings") {
            SettingsScreen(
                goBack = { navController.popBackStack() },
                isDarkTheme = vm.isDarkTheme.value,
                onThemeChange = { vm.switchAppTheme() },
                clearAllData = { vm.clearAllPointsData() }
            )
        }

    }

}