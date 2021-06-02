package com.baymax.weatherforcast.api.response

data class WeatherResponse(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<Record>,
    val message: Int
)