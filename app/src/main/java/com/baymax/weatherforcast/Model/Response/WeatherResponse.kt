package com.baymax.weatherforcast.Model.Response

data class WeatherResponse(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<Record>,
    val message: Int
)