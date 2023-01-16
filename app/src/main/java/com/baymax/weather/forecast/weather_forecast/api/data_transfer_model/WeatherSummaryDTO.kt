package com.baymax.weather.forecast.weather_forecast.api.data_transfer_model

data class WeatherSummaryDTO(
    val description: String?,
    val icon: String?,
    val id: Int?,
    val main: String?
)
