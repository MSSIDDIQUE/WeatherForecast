package com.baymax.weatherforcast.Model.Network.Response


import com.baymax.weatherforcast.Model.DB.City
import com.baymax.weatherforcast.Model.DB.Entity.Record
import com.baymax.weatherforcast.Model.DB.WeatherRecord


data class WeatherResponse(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<Record>,
    val message: Int
)
{

}