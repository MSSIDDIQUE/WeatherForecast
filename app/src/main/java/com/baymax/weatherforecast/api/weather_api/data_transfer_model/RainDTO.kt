package com.baymax.weatherforecast.api.weather_api.data_transfer_model


import com.google.gson.annotations.SerializedName

data class RainDTO(
    @SerializedName("3h")
    val h: Double?
)