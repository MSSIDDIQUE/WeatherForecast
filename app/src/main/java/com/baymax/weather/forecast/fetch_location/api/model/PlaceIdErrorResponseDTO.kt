package com.baymax.weather.forecast.fetch_location.api.model

import com.google.gson.annotations.SerializedName

data class PlaceIdErrorResponseDTO(
    @SerializedName("status")
    val status: String?,
)
