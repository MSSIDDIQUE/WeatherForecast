package com.baymax.weather.forecast.usecases.weather_forecast.data.data_transfer_model

import com.google.gson.annotations.SerializedName

data class RainDTO(
    @SerializedName("3h")
    val h: Double?
)
