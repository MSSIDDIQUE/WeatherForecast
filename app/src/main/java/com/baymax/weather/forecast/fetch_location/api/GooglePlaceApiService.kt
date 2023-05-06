package com.baymax.weather.forecast.fetch_location.api

import com.baymax.weather.forecast.fetch_location.api.model.PlaceIdResponseDTO
import com.baymax.weather.forecast.fetch_location.api.model.PredictionsResponseDTO
import com.baymax.weather.forecast.utils.PrefHelper
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GooglePlaceApiService {

    @GET("autocomplete/json")
    suspend fun getPredictions(
        @Query("input")
        searchText: String,
        @Query(PrefHelper.GOOGLE_PLACE_API_KEY)
        key: String,
    ): Response<PredictionsResponseDTO>

    @GET("details/json")
    suspend fun getCoordinates(
        @Query("placeid")
        placeId: String,
        @Query(PrefHelper.GOOGLE_PLACE_API_KEY)
        key: String,
    ): Response<PlaceIdResponseDTO>
}
