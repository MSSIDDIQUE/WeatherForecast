package com.baymax.weatherforcast.api

import com.baymax.weatherforcast.api.response.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

//http://api.openweathermap.org/data/2.5/forecast?q=Hyderabad,Telangana&appid=5f3f95dcfb462c164ddfce910fffe503
interface WeatherApiService {

    @GET("forecast")
    suspend fun getWeatherOfCity(
        @Query("q")
        location: String
    ): Response<WeatherResponse>

}