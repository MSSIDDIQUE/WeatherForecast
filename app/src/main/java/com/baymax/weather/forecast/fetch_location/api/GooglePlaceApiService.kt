package com.baymax.weather.forecast.fetch_location.api

import com.baymax.weather.forecast.data.ApiResponse
import com.baymax.weather.forecast.fetch_location.api.model.PlaceIdErrorResponseDTO
import com.baymax.weather.forecast.fetch_location.api.model.PlaceIdSuccessResponseDTO
import com.baymax.weather.forecast.fetch_location.api.model.PredictionsErrorResponseDTO
import com.baymax.weather.forecast.fetch_location.api.model.PredictionsSuccessResponseDTO

interface GooglePlaceApiService {
    suspend fun getPredictions(searchText: String): ApiResponse<PredictionsSuccessResponseDTO, PredictionsErrorResponseDTO>
    suspend fun getCoordinates(placeId: String, ): ApiResponse<PlaceIdSuccessResponseDTO, PlaceIdErrorResponseDTO>
}
