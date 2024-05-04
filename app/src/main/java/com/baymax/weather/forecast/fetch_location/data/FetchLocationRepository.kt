package com.baymax.weather.forecast.fetch_location.data

import com.baymax.weather.forecast.data.ApiResponse
import com.baymax.weather.forecast.fetch_location.api.model.PlaceIdErrorResponseDTO
import com.baymax.weather.forecast.fetch_location.api.model.PlaceIdSuccessResponseDTO
import com.baymax.weather.forecast.fetch_location.api.model.PredictionsErrorResponseDTO
import com.baymax.weather.forecast.fetch_location.api.model.PredictionsSuccessResponseDTO
import com.baymax.weather.forecast.fetch_location.presentation.model.CoordinatesDAO

interface FetchLocationRepository {
    suspend fun  getSuggestions(searchText: String): ApiResponse<PredictionsSuccessResponseDTO, PredictionsErrorResponseDTO>
    suspend fun getCoordinates(placeId: String): ApiResponse<PlaceIdSuccessResponseDTO, PlaceIdErrorResponseDTO>
    suspend fun setLastLocation(location: CoordinatesDAO)
    suspend fun getLastLocation(): CoordinatesDAO
    suspend fun isLocationCached(): Boolean
}
