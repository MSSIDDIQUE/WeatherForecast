package com.baymax.weatherforecast.api.weatherApi.dataTransferModel


import com.google.gson.annotations.SerializedName

data class WeatherDTO (
    val clouds: Clouds,
    val dt: Int,
    @SerializedName("dt_txt")
    val dtTxt: String,
    val main: TempInfoDTO,
    val rain: RainDTO,
    val weather: List<WeatherSummaryDTO>,
    val wind: WindDTO
)