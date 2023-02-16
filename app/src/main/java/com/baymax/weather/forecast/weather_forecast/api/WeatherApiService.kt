package com.baymax.weather.forecast.weather_forecast.api

import com.baymax.weather.forecast.utils.PrefHelper
import com.baymax.weather.forecast.weather_forecast.api.model.ApiResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("forecast")
    suspend fun getWeatherOfCity(
        @Query("lat")
        latitude: String,
        @Query("lon")
        longitude: String,
        @Query(PrefHelper.WEATHER_API_KEY)
        key: String,
    ): Response<ApiResponseDTO>
}
