package com.baymax.weatherforecast.api.google_place_api


import com.google.gson.annotations.SerializedName

data class PredictionsResponse(
    @SerializedName("predictions")
    val predictions: List<Prediction>,
    @SerializedName("status")
    val status: String
)