package com.baymax.weather.forecast.usecases.search_location.data

import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchLocationRepositoryImpl @Inject constructor(
    private val searchRemoteDataSource: SearchLocationRemoteDataSource
) : SearchLocationRepository {

    override fun getSuggestions(searchText: String) = flow {
        if (searchText.length > 2) {
            emit(searchRemoteDataSource.fetchPredictions(searchText))
        }
    }

    override suspend fun getCoordinates(placeId: String) = searchRemoteDataSource.fetchCoordinates(placeId)
}
