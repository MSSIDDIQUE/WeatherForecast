package com.baymax.weather.forecast.weather_forecast.api.model

import com.google.gson.annotations.SerializedName

data class WeatherDTO(
    val clouds: Clouds?,
    val dt: Long?,
    @SerializedName("dt_txt")
    val dtTxt: String?,
    val main: TempInfoDTO?,
    val rain: RainDTO?,
    val weather: List<WeatherSummaryDTO>?,
    val wind: WindDTO?,
)
