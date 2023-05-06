package com.baymax.weather.forecast.fetch_location.api.model

import com.google.gson.annotations.SerializedName

data class ResultDTO(
    @SerializedName("geometry")
    val geometry: GeometryDTO?,
)
