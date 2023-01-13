package com.baymax.weather.forecast.usecases.search_location.data.data_transfer_model

import com.google.gson.annotations.SerializedName

data class Geometry(
    @SerializedName("location")
    val location: Location
)
