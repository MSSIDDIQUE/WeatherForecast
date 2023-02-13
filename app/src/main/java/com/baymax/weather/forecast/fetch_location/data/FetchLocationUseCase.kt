package com.baymax.weather.forecast.fetch_location.data

import android.app.Application
import com.baymax.weather.forecast.fetch_location.utils.LocationUtils.locationFlow
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchLocationUseCase @Inject constructor(
    private val repo: FetchLocationRepository,
    private val appContext: Application,
    private val locationProvider: FusedLocationProviderClient,
) {
    suspend fun fetchLocationFromCache() = flow {
        val location = repo.getLastLocation()
        if (location.lat == 0.0 && location.lng == 0.0) emit(null) else emit(location)
    }

    fun fetchLocationFromGps() = locationProvider.locationFlow(appContext)

    suspend fun fetchLocationFromPlaceId(placeId: String) = repo.getCoordinates(placeId)
}
