package com.baymax.weather.forecast.fetch_location.api.model

import com.google.gson.annotations.SerializedName

data class PredictionDTO(
    @SerializedName("description")
    val description: String?,
    @SerializedName("place_id")
    val placeId: String?,
    @SerializedName("reference")
    val reference: String?,
    @SerializedName("types")
    val types: List<String>?,
)
