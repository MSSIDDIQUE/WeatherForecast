package com.baymax.weather.forecast.fetch_location.data

import javax.inject.Inject

class SearchLocationRepositoryImpl @Inject constructor(
    private val searchRemoteDataSource: SearchLocationRemoteDataSource
) : SearchLocationRepository {

    override suspend fun getSuggestions(
        searchText: String
    ) = searchRemoteDataSource.fetchPredictions(searchText)

    override suspend fun getCoordinates(
        placeId: String
    ) = searchRemoteDataSource.fetchCoordinates(placeId)
}
