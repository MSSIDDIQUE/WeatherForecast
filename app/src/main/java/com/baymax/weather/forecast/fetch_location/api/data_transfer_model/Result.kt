package com.baymax.weather.forecast.fetch_location.api.data_transfer_model

import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("geometry")
    val geometry: Geometry,
)
