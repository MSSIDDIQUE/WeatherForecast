package com.baymax.weather.forecast.weather_forecast.data

import com.baymax.weather.forecast.weather_forecast.api.mappers.WeatherDataMapper
import com.baymax.weather.forecast.utils.map
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val remoteDataSource: WeatherRemoteDataSource
) : WeatherRepository {
    override suspend fun fetchWeather(
        lat: Double,
        lng: Double
    ) = remoteDataSource.fetchWeatherForLocation(lat, lng).map { dto ->
        WeatherDataMapper.apiResponseMapper().invoke(dto)
    }
}
