package com.baymax.weather.forecast.api.google_place_api

import com.google.gson.annotations.SerializedName

data class PlaceIdResponse(
    @SerializedName("result")
    val result: Result,
    @SerializedName("status")
    val status: String
)