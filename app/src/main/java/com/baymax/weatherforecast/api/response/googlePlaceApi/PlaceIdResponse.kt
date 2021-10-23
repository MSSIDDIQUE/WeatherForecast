package com.baymax.weatherforecast.api.response.googlePlaceApi


import com.google.gson.annotations.SerializedName

data class PlaceIdResponse(
    @SerializedName("result")
    val result: Result,
    @SerializedName("status")
    val status: String
)