package com.baymax.weatherforcast.Model.Network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.baymax.weatherforcast.Model.Network.Response.WeatherResponse
import com.baymax.weatherforcast.Model.WeatherApiService
import com.baymax.weatherforcast.Utils.Exceptions.NoConnectivityException

class WeatherNetworkAbstractionsImpl(
    private val weatherApiService: WeatherApiService
) : WeatherNetworkAbstractions {

    private val _downloadedWeather = MutableLiveData<WeatherResponse>()
    override val downloadedWeather: LiveData<WeatherResponse>
        get() = _downloadedWeather

    override suspend fun fetchWeather(location: String) {
        try {
            val fetchedWeather = weatherApiService.getWeather(location).await()
            _downloadedWeather.postValue(fetchedWeather)
        }
        catch (e: NoConnectivityException){
            Log.e("Connectivity","No internet connection.",e)
        }
    }
}