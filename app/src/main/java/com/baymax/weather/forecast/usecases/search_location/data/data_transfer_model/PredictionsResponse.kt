package com.baymax.weather.forecast.usecases.search_location.data.data_transfer_model

import com.google.gson.annotations.SerializedName

data class PredictionsResponse(
    @SerializedName("predictions")
    val predictions: List<Prediction>,
    @SerializedName("status")
    val status: String
)
