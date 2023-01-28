package com.baymax.weather.forecast.fetch_location.data

import com.baymax.weather.forecast.data.ResponseWrapper
import com.baymax.weather.forecast.fetch_location.api.data_transfer_model.Location
import com.baymax.weather.forecast.fetch_location.api.data_transfer_model.PlaceIdResponse
import com.baymax.weather.forecast.fetch_location.api.data_transfer_model.PredictionsResponse

interface FetchLocationRepository {
    suspend fun getSuggestions(searchText: String): ResponseWrapper<PredictionsResponse>
    suspend fun getCoordinates(placeId: String): ResponseWrapper<PlaceIdResponse>
    suspend fun setLastLocation(location: Location)
    suspend fun getLastLocation(): Location
    suspend fun isLocationCached(): Boolean
}