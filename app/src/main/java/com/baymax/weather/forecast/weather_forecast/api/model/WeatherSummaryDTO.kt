package com.baymax.weather.forecast.weather_forecast.api.model

data class WeatherSummaryDTO(
    val description: String?,
    val icon: String?,
    val id: Int?,
    val main: String?
)
