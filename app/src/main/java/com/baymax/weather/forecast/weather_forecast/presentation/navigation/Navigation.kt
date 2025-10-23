package com.baymax.weather.forecast.weather_forecast.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.baymax.weather.forecast.weather_forecast.presentation.screens.HomeScreen
import com.baymax.weather.forecast.weather_forecast.presentation.screens.SplashScreen
import com.baymax.weather.forecast.weather_forecast.presentation.viewmodel.HomeScreenViewModel

@Composable
fun AppNavigation(viewModel: HomeScreenViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("home") { HomeScreen(viewModel) }
    }
}