package com.baymax.weatherforcast.Model.Network.Response


import com.google.gson.annotations.SerializedName

data class Rain(
    @SerializedName("3h")
    val h: Double
)