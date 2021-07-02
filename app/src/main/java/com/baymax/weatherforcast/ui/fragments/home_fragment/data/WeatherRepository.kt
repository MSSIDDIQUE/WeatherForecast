package com.baymax.weatherforcast.ui.fragments.home_fragment.data

import com.baymax.weatherforcast.api.response.WeatherResponse
import com.baymax.weatherforcast.data.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class WeatherRepository(
    private val remoteDataSource: WeatherRemoteDataSource,
    private val useCase: LocationProvider
) {
    fun getWeather(city:String?=null): Flow<Result<WeatherResponse>> = flow {
        val interval = 60000L
        useCase.fetchLocation().collect { location ->
            while (true) {
                if(city.isNullOrEmpty()){
                    emit(remoteDataSource.fetchWeatherOfCity(location))
                    delay(interval)
                }
                else{
                    emit(remoteDataSource.fetchWeatherOfCity(city))
                    delay(interval)
                }
            }
        }
    }
}