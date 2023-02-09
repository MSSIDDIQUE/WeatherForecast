package com.baymax.weather.forecast.weather_forecast.presentation.model

data class WeatherDAO(
    val day: String = "",
    val dateTime: String = "",
    val time: String = "",
    val iconSmall: String = "",
    val iconLarge: String = "",
    val windSpeed: String = "",
    val humidityLevel: String = "",
    val temperatureF: String = "",
    val temperatureC: String = "",
    val minTemperatureF: String = "",
    val minTemperatureC: String = "",
    val maxTemperatureF: String = "",
    val maxTemperatureC: String = "",
    val weatherDescription: String = "",
    val isTodayWeatherInfo: Boolean = false,
    val isRecentWeatherInfo: Boolean = false,
)
