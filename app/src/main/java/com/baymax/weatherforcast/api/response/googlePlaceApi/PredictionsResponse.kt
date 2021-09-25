package com.baymax.weatherforcast.api.response.googlePlaceApi


import com.google.gson.annotations.SerializedName

data class PredictionsResponse(
    @SerializedName("predictions")
    val predictions: List<Prediction>,
    @SerializedName("status")
    val status: String
)