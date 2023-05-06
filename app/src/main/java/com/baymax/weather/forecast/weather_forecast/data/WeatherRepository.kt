package com.baymax.weather.forecast.weather_forecast.data

import com.baymax.weather.forecast.data.ResponseWrapper
import com.baymax.weather.forecast.weather_forecast.presentation.model.WeatherReportsDAO

interface WeatherRepository {
    suspend fun fetchWeather(
        lat: Double,
        lng: Double,
    ): ResponseWrapper<out WeatherReportsDAO>
}
