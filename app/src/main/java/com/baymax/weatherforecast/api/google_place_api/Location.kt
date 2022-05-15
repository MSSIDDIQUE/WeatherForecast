package com.baymax.weatherforecast.api.google_place_api


import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lng")
    val lng: Double
)