package com.baymax.weather.forecast.usecases.weather_forecast.data

import com.baymax.weather.forecast.usecases.search_location.data.data_transfer_model.Location
import com.baymax.weather.forecast.usecases.weather_forecast.data.mappers.WeatherDataMapper
import com.baymax.weather.forecast.utils.map
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val remoteDataSource: WeatherRemoteDataSource
) : WeatherRepository {
    override suspend fun getWeather(
        location: Location
    ) = remoteDataSource.fetchWeatherForLocation(location).map { dto ->
        WeatherDataMapper.apiResponseMapper().invoke(dto)
    }
}
