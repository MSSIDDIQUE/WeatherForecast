package com.baymax.weather.forecast.weather_forecast.api.model

data class ApiResponseDTO(
    val city: CityDTO?,
    val cnt: Int?,
    val cod: String?,
    val list: List<WeatherDTO>?,
    val message: Int?,
)
