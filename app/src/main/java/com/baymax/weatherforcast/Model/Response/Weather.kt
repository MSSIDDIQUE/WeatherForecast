package com.baymax.weatherforcast.Model.Response


data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)