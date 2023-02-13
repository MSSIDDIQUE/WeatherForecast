package com.baymax.weather.forecast.weather_forecast.presentation.view_state

import com.baymax.weather.forecast.weather_forecast.presentation.model.WeatherReportsDAO

sealed interface WeatherReportsUiState {
    data class Success(val weatherReports: WeatherReportsDAO) : WeatherReportsUiState
    data class Error(val message: String) : WeatherReportsUiState
    data class Loading(val message: String) : WeatherReportsUiState
    object Idle : WeatherReportsUiState
}
