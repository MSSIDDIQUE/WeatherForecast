package com.baymax.weather.forecast.api.google_place_api

import com.google.gson.annotations.SerializedName

data class Geometry(
    @SerializedName("location")
    val location: Location
)