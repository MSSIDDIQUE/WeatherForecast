package com.baymax.weather.forecast.usecases.search_location.data.data_transfer_model

import com.google.gson.annotations.SerializedName

data class PlaceIdResponse(
    @SerializedName("result")
    val result: Result,
    @SerializedName("status")
    val status: String
)
