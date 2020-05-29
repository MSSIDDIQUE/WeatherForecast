package com.baymax.weatherforcast.Model.DB

import com.baymax.weatherforcast.Model.Network.Response.Coord


data class CityDb(
    val coord: Coord,
    val country: String,
    val id: Int,
    val name: String,
    val population: Int,
    val sunrise: Int,
    val sunset: Int,
    val timezone: Int
)