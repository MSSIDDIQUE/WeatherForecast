package com.baymax.weather.forecast.fetch_location.api.model

import com.google.gson.annotations.SerializedName

data class PredictionsResponseDTO(
    @SerializedName("predictions")
    val predictions: List<PredictionDTO>?,
    @SerializedName("status")
    val status: String?,
)
