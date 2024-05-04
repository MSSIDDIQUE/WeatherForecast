package com.baymax.weather.forecast.weather_forecast.data

import com.baymax.weather.forecast.data.ApiResponse
import com.baymax.weather.forecast.weather_forecast.api.model.ApiResponseDTO

interface WeatherRepository {
    suspend fun fetchWeather(lat: Double, lng: Double): ApiResponse<ApiResponseDTO, ApiResponseDTO>
}
