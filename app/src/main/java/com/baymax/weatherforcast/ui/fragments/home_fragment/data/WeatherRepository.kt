package com.baymax.weatherforcast.ui.fragments.home_fragment.data

import com.baymax.weatherforcast.api.response.googlePlaceApi.Location
import com.baymax.weatherforcast.api.response.weatherApi.WeatherResponse
import com.baymax.weatherforcast.data.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class WeatherRepository(
    private val remoteDataSource: WeatherRemoteDataSource,
    private val useCase: LocationProvider
) {
    fun getWeather(
        searchedLocation: Location? = null
    ): Flow<Result<WeatherResponse>> = flow {
        useCase.fetchLocation().collect { deviceLocation ->
            searchedLocation?.let { providedLocation ->
                emit(
                    remoteDataSource.fetchWeatherForLocation(
                        providedLocation.lat.toString(),
                        providedLocation.lng.toString()
                    )
                )
            } ?: emit(
                remoteDataSource.fetchWeatherForLocation(
                    deviceLocation.latitude.toString(),
                    deviceLocation.longitude.toString()
                )
            )
        }
    }

    fun getSuggestions(searchText: String) = flow {
        if (searchText.length > 2) {
            delay(1000)
            emit(remoteDataSource.fetchPredictions(searchText))
        }
    }

    suspend fun getCoordinates(placeId: String) = remoteDataSource.fetchCoordinates(placeId)

}