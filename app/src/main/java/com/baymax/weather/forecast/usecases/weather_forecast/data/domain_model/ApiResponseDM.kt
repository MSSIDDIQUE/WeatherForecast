package com.baymax.weather.forecast.usecases.weather_forecast.data.domain_model

data class ApiResponseDM(
    val city: CityDM,
    val cnt: Int,
    val cod: String,
    val dataGroupedByDate: Map<String, List<WeatherDM>>,
    val dataGroupedByTime: Map<String, List<WeatherDM>>,
    val message: Int
) {
    data class CityDM(
        val cityLat: Double,
        val cityLon: Double,
        val country: String,
        val id: Int,
        val name: String,
        val population: Int,
        val sunrise: Int,
        val sunset: Int,
        val timezone: Int
    )
}
