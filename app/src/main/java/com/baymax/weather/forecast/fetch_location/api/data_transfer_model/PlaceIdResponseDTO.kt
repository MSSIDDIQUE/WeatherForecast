package com.baymax.weather.forecast.fetch_location.api.data_transfer_model

import com.google.gson.annotations.SerializedName

data class PlaceIdResponseDTO(
    @SerializedName("result")
    val result: ResultDTO?,
    @SerializedName("status")
    val status: String?,
)
