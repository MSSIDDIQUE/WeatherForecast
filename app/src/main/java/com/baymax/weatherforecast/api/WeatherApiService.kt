package com.baymax.weatherforecast.api

import com.baymax.weatherforecast.api.response.weatherApi.WeatherResponse
import com.baymax.weatherforecast.utils.PrefHelper
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

//http://api.openweathermap.org/data/2.5/forecast?q=Hyderabad,Telangana&appid=5f3f95dcfb462c164ddfce910fffe503
interface WeatherApiService {

    @GET("forecast")
    suspend fun getWeatherOfCity(
        @Query("lat")
        latitude: String,
        @Query("lon")
        longitude: String,
        @Query(PrefHelper.WEATHER_API_KEY)
        key: String
    ): Response<WeatherResponse>

}