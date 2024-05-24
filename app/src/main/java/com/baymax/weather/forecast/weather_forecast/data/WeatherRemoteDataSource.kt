package com.baymax.weather.forecast.weather_forecast.data

import com.baymax.weather.forecast.weather_forecast.api.WeatherApiService
import javax.inject.Inject

open class WeatherRemoteDataSource @Inject constructor(
    private val weatherApiService: WeatherApiService
) {
    suspend fun fetchWeatherForLocation(
        lat: Double,
        lng: Double
    ) = weatherApiService.getWeatherOfCity(
        lat.toString(),
        lng.toString()
    )
}
