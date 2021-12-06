package com.baymax.weatherforecast.ui.fragments.home_fragment.data

import com.baymax.weatherforecast.api.googlePlaceApi.Location
import com.baymax.weatherforecast.api.weatherApi.mappers.WeatherDataMapper
import com.baymax.weatherforecast.data.Result
import com.baymax.weatherforecast.utils.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

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