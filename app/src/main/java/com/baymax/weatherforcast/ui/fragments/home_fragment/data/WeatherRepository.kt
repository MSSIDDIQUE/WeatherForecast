package com.baymax.weatherforcast.ui.fragments.home_fragment.data

import com.baymax.weatherforcast.api.response.googlePlaceApi.Location
import kotlinx.coroutines.flow.flow

class WeatherRepository(
    private val remoteDataSource: WeatherRemoteDataSource
) {
    suspend fun getWeather(
        location: Location
    ) = remoteDataSource.fetchWeatherForLocation(location)

    fun getSuggestions(searchText: String) = flow {
        if (searchText.length > 2) {
            emit(remoteDataSource.fetchPredictions(searchText))
        }
    }

    suspend fun getCoordinates(placeId: String) = remoteDataSource.fetchCoordinates(placeId)

}