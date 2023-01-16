package com.baymax.weather.forecast.search_location.api.data_transfer_model

import com.google.gson.annotations.SerializedName

data class Geometry(
    @SerializedName("location")
    val location: Location
)
