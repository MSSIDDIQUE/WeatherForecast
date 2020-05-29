package com.baymax.weatherforcast.Model.Network.Response


import com.google.gson.annotations.SerializedName

data class Wind(
    val deg: Int,
    val speed: Double
)