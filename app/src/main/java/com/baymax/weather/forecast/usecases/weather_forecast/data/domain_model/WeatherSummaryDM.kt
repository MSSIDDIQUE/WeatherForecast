package com.baymax.weather.forecast.usecases.weather_forecast.data.domain_model

data class WeatherSummaryDM(
    val description: String,
    val iconSmall: String,
    val iconLarge: String,
    val id: Int,
    val main: String
)
