package com.baymax.weather.forecast.weather_forecast.api.model

import com.google.gson.annotations.SerializedName

data class RainDTO(
    @SerializedName("3h")
    val h: Double?
)
