package com.baymax.weatherforcast.ui.fragments.home_fragment.ui

import android.util.Log
import androidx.lifecycle.*
import com.baymax.weatherforcast.api.response.WeatherResponse
import com.baymax.weatherforcast.ui.fragments.home_fragment.data.LocationProviderUseCase
import com.baymax.weatherforcast.ui.fragments.home_fragment.data.WeatherData
import com.baymax.weatherforcast.ui.fragments.home_fragment.data.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFramentViewModel(
    private val useCase: LocationProviderUseCase,
    private val repo: WeatherRepository): ViewModel() {
    var isGpsEnabled = MutableLiveData<Boolean>()

    fun setGpsStatus(value:Boolean){
        isGpsEnabled.postValue(value)
    }
    val location by lazy {
        useCase.fetchUpdates().asLiveData()
    }

/*

    val weatherData by lazy {
        liveData(Dispatchers.IO) {
            val data = repo.getWeather()
            emitSource(data)
        }
    }
*/

    fun getWeatherOfCity(
        city:String
    )=liveData(Dispatchers.IO) {
            emit(repo.getWeatherOfCity(city))
    }
}