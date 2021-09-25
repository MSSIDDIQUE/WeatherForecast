package com.baymax.weatherforcast.api

import com.baymax.weatherforcast.api.response.googlePlaceApi.PlaceIdResponse
import com.baymax.weatherforcast.api.response.googlePlaceApi.PredictionsResponse
import com.baymax.weatherforcast.utils.PrefHelper
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GooglePlaceApiService {

    @GET("autocomplete/json")
    suspend fun getPredictions(
        @Query("input")
        searchText: String,
        @Query(PrefHelper.GOOGLE_PLACE_API_KEY)
        key: String
    ): Response<PredictionsResponse>

    @GET("details/json")
    suspend fun getCoordinates(
        @Query("placeid")
        placeId: String,
        @Query(PrefHelper.GOOGLE_PLACE_API_KEY)
        key: String
    ): Response<PlaceIdResponse>
}