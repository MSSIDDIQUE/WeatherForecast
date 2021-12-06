package com.baymax.weatherforecast.api.weatherApi.dataTransferModel


import com.google.gson.annotations.SerializedName

data class RainDTO(
    @SerializedName("3h")
    val h: Double
)