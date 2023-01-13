package com.baymax.weather.forecast.usecases.weather_forecast.data.data_transfer_model

data class ApiResponseDTO(
    val city: CityDTO?,
    val cnt: Int?,
    val cod: String?,
    val list: List<WeatherDTO>?,
    val message: Int?
)
