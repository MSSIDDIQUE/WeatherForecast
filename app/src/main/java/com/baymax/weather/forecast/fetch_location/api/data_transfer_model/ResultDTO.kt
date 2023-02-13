package com.baymax.weather.forecast.fetch_location.api.data_transfer_model

import com.google.gson.annotations.SerializedName

data class ResultDTO(
    @SerializedName("geometry")
    val geometry: GeometryDTO?,
)
