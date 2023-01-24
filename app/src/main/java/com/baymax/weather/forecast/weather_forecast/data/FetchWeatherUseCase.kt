package com.baymax.weather.forecast.weather_forecast.data

import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class FetchWeatherUseCase @Inject constructor(private val repo: WeatherRepository) {
    suspend operator fun invoke(lat: Double, lng: Double) = flowOf(repo.fetchWeather(lat, lng))
}
