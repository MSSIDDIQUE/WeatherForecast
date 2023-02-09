package com.baymax.weather.forecast.weather_forecast.api

import com.baymax.weather.forecast.utils.PrefHelper
import com.baymax.weather.forecast.weather_forecast.api.model.ApiResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

// http://api.openweathermap.org/data/2.5/forecast?q=Hyderabad,Telangana&appid=5f3f95dcfb462c164ddfce910fffe503
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
