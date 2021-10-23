package com.baymax.weatherforecast.api.response.weatherApi


import com.google.gson.annotations.SerializedName

data class Rain(
    @SerializedName("3h")
    val h: Double
)