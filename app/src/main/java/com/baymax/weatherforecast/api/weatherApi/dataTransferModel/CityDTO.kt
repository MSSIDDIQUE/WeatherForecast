package com.baymax.weatherforecast.api.weatherApi.dataTransferModel


data class CityDTO(
    val coord: Coord,
    val country: String,
    val id: Int,
    val name: String,
    val population: Int,
    val sunrise: Int,
    val sunset: Int,
    val timezone: Int
)