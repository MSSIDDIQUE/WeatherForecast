package com.baymax.weather.forecast.weather_forecast.data

import com.baymax.weather.forecast.data.BaseDataSource
import com.baymax.weather.forecast.weather_forecast.api.WeatherApiService
import com.baymax.weather.forecast.utils.PrefHelper
import com.baymax.weather.forecast.utils.get
import javax.inject.Inject

class WeatherRemoteDataSource @Inject constructor(
    private val weatherApiService: WeatherApiService,
    private val prefHelper: PrefHelper
) : BaseDataSource() {

    suspend fun fetchWeatherForLocation(
        lat: Double,
        lng: Double,
        apiKey: String
    ) = getResult {
        weatherApiService.getWeatherOfCity(
            lat.toString(),
            lng.toString(),
            apiKey
        )
    }
}
