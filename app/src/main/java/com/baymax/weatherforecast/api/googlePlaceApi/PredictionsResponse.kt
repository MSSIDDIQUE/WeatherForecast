package com.baymax.weatherforecast.api.googlePlaceApi


import com.google.gson.annotations.SerializedName

data class PredictionsResponse(
    @SerializedName("predictions")
    val predictions: List<Prediction>,
    @SerializedName("status")
    val status: String
)