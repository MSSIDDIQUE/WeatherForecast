package com.baymax.weatherforecast.api.googlePlaceApi


import com.google.gson.annotations.SerializedName

data class Geometry(
    @SerializedName("location")
    val location: Location
)