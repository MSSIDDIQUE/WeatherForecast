package com.baymax.weather.forecast.fetch_location.api.model

import com.google.gson.annotations.SerializedName

data class PredictionsErrorResponseDTO(
    @SerializedName("error_message")
    val errorMessage: String?,
)
