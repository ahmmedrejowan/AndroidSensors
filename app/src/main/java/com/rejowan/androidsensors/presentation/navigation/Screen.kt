package com.rejowan.androidsensors.presentation.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object SensorDetail : Screen("sensor/{sensorType}") {
        fun createRoute(sensorType: Int) = "sensor/$sensorType"
    }
    data object Settings : Screen("settings")
}
