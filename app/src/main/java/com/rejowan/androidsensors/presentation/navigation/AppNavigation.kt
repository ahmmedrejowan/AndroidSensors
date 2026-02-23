package com.rejowan.androidsensors.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rejowan.androidsensors.presentation.screens.home.HomeScreen
import com.rejowan.androidsensors.presentation.screens.sensor.SensorDetailScreen
import com.rejowan.androidsensors.presentation.screens.settings.SettingsScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onSensorClick = { sensorType ->
                    navController.navigate(Screen.SensorDetail.createRoute(sensorType))
                },
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }

        composable(
            route = Screen.SensorDetail.route,
            arguments = listOf(
                navArgument("sensorType") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val sensorType = backStackEntry.arguments?.getInt("sensorType") ?: 0
            SensorDetailScreen(
                sensorType = sensorType,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
