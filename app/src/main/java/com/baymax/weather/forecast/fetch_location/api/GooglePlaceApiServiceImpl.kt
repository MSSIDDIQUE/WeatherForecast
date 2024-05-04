package com.baymax.weather.forecast.fetch_location.api

import com.baymax.weather.forecast.data.ApiResponse
import com.baymax.weather.forecast.fetch_location.api.model.PlaceIdErrorResponseDTO
import com.baymax.weather.forecast.fetch_location.api.model.PlaceIdSuccessResponseDTO
import com.baymax.weather.forecast.fetch_location.api.model.PredictionsErrorResponseDTO
import com.baymax.weather.forecast.fetch_location.api.model.PredictionsSuccessResponseDTO
import com.baymax.weather.forecast.utils.safeRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.http.HttpMethod
import io.ktor.http.path

class GooglePlaceApiServiceImpl(private val client: HttpClient) : GooglePlaceApiService {
    override suspend fun getPredictions(searchText: String): ApiResponse<PredictionsSuccessResponseDTO, PredictionsErrorResponseDTO> = client.safeRequest {
        url {
            method = HttpMethod.Get
            path("autocomplete/json")
        }
        parameter("input", searchText)
    }

    override suspend fun getCoordinates(placeId: String): ApiResponse<PlaceIdSuccessResponseDTO, PlaceIdErrorResponseDTO> = client.safeRequest {
        url {
            method = HttpMethod.Get
            path("details/json")
        }
        parameter("place_id", placeId)
    }
}
