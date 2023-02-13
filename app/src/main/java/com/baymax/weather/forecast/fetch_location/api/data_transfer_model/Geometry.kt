package com.baymax.weather.forecast.fetch_location.api.data_transfer_model

import com.google.gson.annotations.SerializedName

data class Geometry(
    @SerializedName("location")
    val location: Location,
)
