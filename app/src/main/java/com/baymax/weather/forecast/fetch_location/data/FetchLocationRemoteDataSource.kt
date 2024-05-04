package com.baymax.weather.forecast.fetch_location.data

import com.baymax.weather.forecast.fetch_location.api.GooglePlaceApiService
import javax.inject.Inject

class FetchLocationRemoteDataSource @Inject constructor(private val placesApiService: GooglePlaceApiService) {
    suspend fun fetchPredictions(searchText: String) = placesApiService.getPredictions(searchText)
    suspend fun fetchCoordinates(placeId: String) = placesApiService.getCoordinates(placeId)
}
