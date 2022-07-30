package com.baymax.weather.forecast.data

import com.baymax.weather.forecast.api.google_place_api.Location
import com.baymax.weather.forecast.api.weather_api.mappers.WeatherDataMapper
import com.baymax.weather.forecast.utils.map
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