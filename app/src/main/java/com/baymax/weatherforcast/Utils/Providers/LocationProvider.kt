package com.baymax.weatherforcast.Utils.Providers

import com.baymax.weatherforcast.Model.DB.Entity.RecordDb
import com.baymax.weatherforcast.Model.DB.WeatherData

interface LocationProvider {
    suspend fun hasLocationChanged(lastWeatherLocation: List<WeatherData>): Boolean
    suspend fun getPreferredLocationString(): String
}