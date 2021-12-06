package com.baymax.weatherforecast.api.googlePlaceApi


import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("geometry")
    val geometry: Geometry,
)