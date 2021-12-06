package com.baymax.weatherforecast.api.weatherApi.dataTransferModel


data class WeatherSummaryDTO(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)