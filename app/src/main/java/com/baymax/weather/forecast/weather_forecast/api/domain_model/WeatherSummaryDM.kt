package com.baymax.weather.forecast.weather_forecast.api.domain_model

data class WeatherSummaryDM(
    val description: String,
    val iconSmall: String,
    val iconLarge: String,
    val id: Int,
    val main: String
)
