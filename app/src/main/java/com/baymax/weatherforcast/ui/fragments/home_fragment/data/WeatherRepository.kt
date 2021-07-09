package com.baymax.weatherforcast.ui.fragments.home_fragment.data

import com.baymax.weatherforcast.api.response.WeatherResponse
import com.baymax.weatherforcast.data.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class WeatherRepository(
    private val remoteDataSource: WeatherRemoteDataSource,
    private val useCase: LocationProvider
) {
    @ExperimentalCoroutinesApi
    fun getWeather(city: String? = null): Flow<Result<WeatherResponse>> = flow {
        useCase.fetchLocation().collect { location ->
            if (city.isNullOrEmpty()) {
                emit(remoteDataSource.fetchWeatherOfCity(location))
            } else {
                emit(remoteDataSource.fetchWeatherOfCity(city))
            }
        }
    }
}