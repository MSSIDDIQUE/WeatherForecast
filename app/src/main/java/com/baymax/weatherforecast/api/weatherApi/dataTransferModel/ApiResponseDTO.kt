package com.baymax.weatherforecast.api.weatherApi.dataTransferModel

data class ApiResponseDTO(
    val city: CityDTO,
    val cnt: Int,
    val cod: String,
    val list: List<WeatherDTO>,
    val message: Int
)