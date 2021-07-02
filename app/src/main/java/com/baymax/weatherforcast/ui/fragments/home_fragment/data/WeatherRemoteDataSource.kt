package com.baymax.weatherforcast.ui.fragments.home_fragment.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.baymax.weatherforcast.api.BaseDataSource
import com.baymax.weatherforcast.api.response.WeatherResponse
import com.baymax.weatherforcast.api.WeatherApiService
import com.baymax.weatherforcast.utils.exceptions.NoConnectivityException

class WeatherRemoteDataSource(
    private val weatherApiService: WeatherApiService
) : BaseDataSource() {
    suspend fun fetchWeatherOfCity(city:String) = getResult{
        Log.d("Saquib","triggering the api for latest weather")
        weatherApiService.getWeatherOfCity(city)
    }
}