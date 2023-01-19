package com.baymax.weather.forecast.weather_forecast.data

import com.baymax.weather.forecast.utils.PrefHelper
import com.baymax.weather.forecast.utils.get
import com.baymax.weather.forecast.utils.map
import com.baymax.weather.forecast.weather_forecast.api.mappers.WeatherDataMapper
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val prefHelper: PrefHelper,
    private val remoteDataSource: WeatherRemoteDataSource
) : WeatherRepository {
    override suspend fun fetchWeather(
        lat: Double,
        lng: Double
    ) = remoteDataSource.fetchWeatherForLocation(
        lat,
        lng,
        prefHelper.sharedPrefs[PrefHelper.WEATHER_API_KEY, ""]
    ).map { dto ->
        WeatherDataMapper.apiResponseMapper().invoke(dto)
    }
}
