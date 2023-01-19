package com.baymax.weather.forecast.fetch_location.data

import com.baymax.weather.forecast.data.BaseDataSource
import com.baymax.weather.forecast.fetch_location.api.GooglePlaceApiService
import javax.inject.Inject

class FetchLocationRemoteDataSource @Inject constructor(
    private val placesApiService: GooglePlaceApiService
) : BaseDataSource() {

    suspend fun fetchPredictions(
        searchText: String,
        apiKey: String
    ) = getResult { placesApiService.getPredictions(searchText, apiKey) }

    suspend fun fetchCoordinates(
        placeId: String,
        apiKey: String
    ) = getResult { placesApiService.getCoordinates(placeId, apiKey) }
}
