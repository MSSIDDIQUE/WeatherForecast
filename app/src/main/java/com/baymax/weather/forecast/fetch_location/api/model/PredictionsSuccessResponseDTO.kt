package com.baymax.weather.forecast.fetch_location.api.model

import com.google.gson.annotations.SerializedName

data class PredictionsSuccessResponseDTO(
    @SerializedName("predictions")
    val predictions: List<PredictionDTO>?,
    @SerializedName("status")
    val status: String?,
)
