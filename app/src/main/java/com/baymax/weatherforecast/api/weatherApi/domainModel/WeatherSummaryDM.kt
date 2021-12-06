package com.baymax.weatherforecast.api.weatherApi.domainModel

data class WeatherSummaryDM(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)