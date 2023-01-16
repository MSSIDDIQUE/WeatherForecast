package com.baymax.weather.forecast.search_location.data

import com.baymax.weather.forecast.data.ResponseWrapper
import com.baymax.weather.forecast.search_location.api.data_transfer_model.PlaceIdResponse
import com.baymax.weather.forecast.search_location.api.data_transfer_model.PredictionsResponse

interface SearchLocationRepository {
    suspend fun getSuggestions(searchText: String): ResponseWrapper<PredictionsResponse>
    suspend fun getCoordinates(placeId: String): ResponseWrapper<PlaceIdResponse>
}
