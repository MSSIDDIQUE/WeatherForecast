package com.baymax.weatherforcast.api.response.weatherApi


import com.google.gson.annotations.SerializedName

data class Record (
    val clouds: Clouds,
    val dt: Int,
    @SerializedName("dt_txt")
    val dtTxt: String,
    val main: Main,
    val rain: Rain,
    val weather: List<Weather>,
    val wind: Wind
)