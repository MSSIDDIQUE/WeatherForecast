package com.baymax.weatherforcast.Model.Network.Response

import com.baymax.weatherforcast.Model.Network.Response.Record
import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<Record>,
    val message: Int
)