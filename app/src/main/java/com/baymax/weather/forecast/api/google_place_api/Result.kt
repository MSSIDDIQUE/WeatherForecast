package com.baymax.weather.forecast.api.google_place_api

import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("geometry")
    val geometry: Geometry,
)