package com.baymax.weather.forecast.fetch_location.api.model

import com.google.gson.annotations.SerializedName

data class PlaceIdSuccessResponseDTO(
    @SerializedName("result")
    val result: ResultDTO?,
    @SerializedName("status")
    val status: String?,
)
