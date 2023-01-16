package com.baymax.weather.forecast.weather_forecast.data

import com.baymax.weather.forecast.data.ResponseWrapper
import com.baymax.weather.forecast.weather_forecast.api.domain_model.ApiResponseDM

interface WeatherRepository {
    suspend fun fetchWeather(
        lat: Double,
        lng: Double
    ): ResponseWrapper<ApiResponseDM>
}
