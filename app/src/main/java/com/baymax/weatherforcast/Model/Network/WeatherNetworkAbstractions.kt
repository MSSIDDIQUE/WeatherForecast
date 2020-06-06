package com.baymax.weatherforcast.Model.Network

import androidx.lifecycle.LiveData
import com.baymax.weatherforcast.Model.Response.WeatherResponse

interface WeatherNetworkAbstractions {
    val downloadedWeather: LiveData<WeatherResponse>
    suspend fun fetchWeather(
        location:String
    )
}