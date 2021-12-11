package com.baymax.weatherforecast.api.weatherApi.domainModel

data class WeatherSummaryDM(
    val description: String,
    val iconSmall: String,
    val iconLarge: String,
    val id: Int,
    val main: String
)