package com.baymax.weather.forecast.usecases.weather_forecast.data.data_transfer_model

data class CityDTO(
    val coord: Coord?,
    val country: String?,
    val id: Int?,
    val name: String?,
    val population: Int?,
    val sunrise: Int?,
    val sunset: Int?,
    val timezone: Int?
)
