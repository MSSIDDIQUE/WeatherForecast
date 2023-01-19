package com.baymax.weather.forecast.weather_forecast.data

import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchWeatherUseCase @Inject constructor(private val repo: WeatherRepository) {
    operator fun invoke(lat: Double, lng: Double) = flow {
        if (lat != 0.0 && lng != 0.0) {
            emit(repo.fetchWeather(lat, lng))
        }
    }
}
