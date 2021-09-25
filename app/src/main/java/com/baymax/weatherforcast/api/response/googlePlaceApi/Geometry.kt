package com.baymax.weatherforcast.api.response.googlePlaceApi


import com.google.gson.annotations.SerializedName

data class Geometry(
    @SerializedName("location")
    val location: Location
)