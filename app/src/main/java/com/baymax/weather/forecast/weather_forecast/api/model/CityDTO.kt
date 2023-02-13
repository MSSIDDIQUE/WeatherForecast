package com.baymax.weather.forecast.weather_forecast.api.model

data class CityDTO(
    val coord: Coord?,
    val country: String?,
    val id: Int?,
    val name: String?,
    val population: Int?,
    val sunrise: Long?,
    val sunset: Long?,
    val timezone: Int?,
)
