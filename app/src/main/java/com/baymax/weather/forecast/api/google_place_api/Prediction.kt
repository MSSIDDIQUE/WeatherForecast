package com.baymax.weather.forecast.api.google_place_api

import com.google.gson.annotations.SerializedName

data class Prediction(
    @SerializedName("description")
    val description: String,
    @SerializedName("place_id")
    val placeId: String,
    @SerializedName("reference")
    val reference: String,
    @SerializedName("types")
    val types: List<String>
)