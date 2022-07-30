package com.baymax.weather.forecast.api.weather_api.domain_model

data class WeatherSummaryDM(
    val description: String,
    val iconSmall: String,
    val iconLarge: String,
    val id: Int,
    val main: String
)