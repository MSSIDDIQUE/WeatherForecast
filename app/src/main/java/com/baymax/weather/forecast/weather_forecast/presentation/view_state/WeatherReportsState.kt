package com.baymax.weather.forecast.weather_forecast.presentation.view_state

import com.baymax.weather.forecast.weather_forecast.presentation.model.WeatherReportsDAO

sealed interface WeatherReportsState {
    data class Success(val weatherReports: WeatherReportsDAO) : WeatherReportsState
    data class Error(val message: String) : WeatherReportsState
    data class Loading(val message: String) : WeatherReportsState
    object Idle : WeatherReportsState
}
