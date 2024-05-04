package com.baymax.weather.forecast.weather_forecast.api

import com.baymax.weather.forecast.data.ApiResponse
import com.baymax.weather.forecast.weather_forecast.api.model.ApiResponseDTO

interface WeatherApiService {
    suspend fun getWeatherOfCity(
        latitude: String,
        longitude: String
    ): ApiResponse<ApiResponseDTO, ApiResponseDTO>
}