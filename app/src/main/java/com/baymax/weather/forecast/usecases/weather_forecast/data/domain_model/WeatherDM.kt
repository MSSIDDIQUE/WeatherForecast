package com.baymax.weather.forecast.usecases.weather_forecast.data.domain_model

data class WeatherDM(
    val dt: Int,
    val dtTxt: String,
    val temp: TempInfoDM,
    val weatherSummary: WeatherSummaryDM,
    val wind: WindDM
) {
    data class TempInfoDM(
        val feelsLike: Double,
        val grndLevel: Int,
        val humidity: String,
        val pressure: Int,
        val seaLevel: Int,
        val temp: String,
        val tempKf: Double,
        val tempMax: String,
        val tempMin: String
    )

    data class RainDM(
        val height: Double
    )

    data class WindDM(
        val angle: Int,
        val speed: Double
    )
}
