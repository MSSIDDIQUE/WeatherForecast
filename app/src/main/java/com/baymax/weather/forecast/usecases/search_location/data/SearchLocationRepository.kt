package com.baymax.weather.forecast.usecases.search_location.data

import com.baymax.weather.forecast.data.ResponseWrapper
import com.baymax.weather.forecast.usecases.search_location.data.data_transfer_model.PlaceIdResponse
import com.baymax.weather.forecast.usecases.search_location.data.data_transfer_model.PredictionsResponse
import kotlinx.coroutines.flow.Flow

interface SearchLocationRepository {
    fun getSuggestions(searchText: String): Flow<ResponseWrapper<PredictionsResponse>>
    suspend fun getCoordinates(placeId: String): ResponseWrapper<PlaceIdResponse>
}
