package com.baymax.weather.forecast.fetch_location.api.model

import com.google.gson.annotations.SerializedName

data class GeometryDTO(
    @SerializedName("location")
    val location: LocationDTO?,
)
