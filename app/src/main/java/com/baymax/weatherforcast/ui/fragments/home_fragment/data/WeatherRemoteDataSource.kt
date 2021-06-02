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

    private val _downloadedWeather = MutableLiveData<WeatherResponse>()
    val downloadedWeather: LiveData<WeatherResponse>
        get() = _downloadedWeather

    suspend fun fetchWeather(location: String) {
        try {
            val fetchedWeather = weatherApiService.getWeather(location).await()
            _downloadedWeather.postValue(fetchedWeather)
        }
        catch (e: NoConnectivityException){
            Log.e("Connectivity","No internet connection.",e)
        }
    }

    suspend fun fetchWeatherOfCity(city:String) = getResult{
        weatherApiService.getWeatherOfCity(city)
    }
}