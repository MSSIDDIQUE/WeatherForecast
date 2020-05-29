package com.baymax.weatherforcast.Model.Repository

import androidx.lifecycle.LiveData
import com.baymax.weatherforcast.Model.DB.WeatherData

interface Repository {
    suspend fun getWeather():LiveData<WeatherData>
}