package com.baymax.weatherforcast.api.response.googlePlaceApi


import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("geometry")
    val geometry: Geometry,
)