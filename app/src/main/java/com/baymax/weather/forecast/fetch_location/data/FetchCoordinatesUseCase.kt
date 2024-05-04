package com.baymax.weather.forecast.fetch_location.data

import android.app.Application
import com.baymax.weather.forecast.data.ApiResponse
import com.baymax.weather.forecast.fetch_location.api.mappers.GooglePlacesDataMapper.toCoordinatesDAO
import com.baymax.weather.forecast.fetch_location.presentation.model.LocationCoordinatesState
import com.baymax.weather.forecast.fetch_location.utils.LocationUtils.locationFlow
import com.google.android.gms.location.FusedLocationProviderClient
import javax.inject.Inject

class FetchCoordinatesUseCase @Inject constructor(
    private val repo: FetchLocationRepository,
    private val appContext: Application,
    private val locationProvider: FusedLocationProviderClient,
) {
    suspend fun fetchLocationFromCache() = repo.getLastLocation()
    fun fetchLocationFromGps() = locationProvider.locationFlow(appContext)
    suspend fun fetchLocationFromPlaceId(placeId: String) =
        when (val result = repo.getCoordinates(placeId)) {
            is ApiResponse.Error.GenericError -> LocationCoordinatesState.Error(result.errorMessage)
            is ApiResponse.Error.HttpError -> LocationCoordinatesState.Error(result.errorMessage)
            is ApiResponse.Error.SerializationError -> LocationCoordinatesState.Error(result.errorMessage)
            is ApiResponse.Success -> LocationCoordinatesState.Found(toCoordinatesDAO(result.body))
        }
}
