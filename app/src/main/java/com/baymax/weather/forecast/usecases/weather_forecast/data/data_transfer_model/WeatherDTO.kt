package com.baymax.weather.forecast.usecases.weather_forecast.data.data_transfer_model

import com.google.gson.annotations.SerializedName

data class WeatherDTO(
    val clouds: Clouds?,
    val dt: Int?,
    @SerializedName("dt_txt")
    val dtTxt: String?,
    val main: TempInfoDTO?,
    val rain: RainDTO?,
    val weather: List<WeatherSummaryDTO>?,
    val wind: WindDTO?
)
