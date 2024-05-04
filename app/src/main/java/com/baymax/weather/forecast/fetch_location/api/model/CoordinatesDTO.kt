package com.baymax.weather.forecast.fetch_location.api.model

import com.google.gson.annotations.SerializedName

data class CoordinatesDTO(
    @SerializedName("lat")
    val lat: Double?,
    @SerializedName("lng")
    val lng: Double?,
)
