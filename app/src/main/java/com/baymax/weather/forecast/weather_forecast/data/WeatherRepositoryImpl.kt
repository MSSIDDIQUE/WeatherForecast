package com.baymax.weather.forecast.weather_forecast.data

import com.baymax.weather.forecast.utils.PrefHelper
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val prefHelper: PrefHelper,
    private val remoteDataSource: WeatherRemoteDataSource,
) : WeatherRepository {
    override suspend fun fetchWeather(lat: Double, lng: Double) = remoteDataSource.fetchWeatherForLocation(lat, lng)
}
