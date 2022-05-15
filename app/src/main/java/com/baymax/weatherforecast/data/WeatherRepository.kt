package com.baymax.weatherforecast.data

import com.baymax.weatherforecast.api.google_place_api.Location
import com.baymax.weatherforecast.api.weather_api.mappers.WeatherDataMapper
import com.baymax.weatherforecast.utils.map
import kotlinx.coroutines.flow.flow

class WeatherRepository(
    private val remoteDataSource: WeatherRemoteDataSource
) {
    suspend fun getWeather(
        location: Location
    ) = remoteDataSource.fetchWeatherForLocation(location).map { dto ->
        WeatherDataMapper.apiResponseMapper().invoke(dto)
    }

    fun getSuggestions(searchText: String) = flow {
        if (searchText.length > 2) {
            emit(remoteDataSource.fetchPredictions(searchText))
        }
    }

    suspend fun getCoordinates(placeId: String) = remoteDataSource.fetchCoordinates(placeId)

}