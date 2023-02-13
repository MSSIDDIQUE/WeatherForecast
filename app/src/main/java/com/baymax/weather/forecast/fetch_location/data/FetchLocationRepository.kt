package com.baymax.weather.forecast.fetch_location.data

import com.baymax.weather.forecast.data.ResponseWrapper
import com.baymax.weather.forecast.fetch_location.presentation.model.LocationDAO
import com.baymax.weather.forecast.fetch_location.presentation.model.PredictionDAO

interface FetchLocationRepository {
    suspend fun getSuggestions(searchText: String): ResponseWrapper<List<PredictionDAO>>
    suspend fun getCoordinates(placeId: String): ResponseWrapper<LocationDAO>
    suspend fun setLastLocation(location: LocationDAO)
    suspend fun getLastLocation(): LocationDAO
    suspend fun isLocationCached(): Boolean
}
