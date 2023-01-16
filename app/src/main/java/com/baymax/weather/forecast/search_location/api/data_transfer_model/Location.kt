package com.baymax.weather.forecast.search_location.api.data_transfer_model

import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lng")
    val lng: Double
)
