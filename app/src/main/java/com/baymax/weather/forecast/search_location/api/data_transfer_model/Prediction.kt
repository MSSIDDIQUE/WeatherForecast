package com.baymax.weather.forecast.search_location.api.data_transfer_model

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
