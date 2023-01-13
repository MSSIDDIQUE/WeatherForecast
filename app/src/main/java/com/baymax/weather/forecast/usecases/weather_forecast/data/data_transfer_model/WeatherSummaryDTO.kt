package com.baymax.weather.forecast.usecases.weather_forecast.data.data_transfer_model

data class WeatherSummaryDTO(
    val description: String?,
    val icon: String?,
    val id: Int?,
    val main: String?
)
