package com.baymax.weatherforcast.ui.fragments.home_fragment.ui

import androidx.lifecycle.*
import com.baymax.weatherforcast.ui.fragments.home_fragment.data.WeatherRepository

class HomeFramentViewModel(
    private val repo: WeatherRepository
) : ViewModel() {
    var isGpsEnabled = MutableLiveData<Boolean>()
    var isNetworkAvailable = MutableLiveData<Boolean>()
    var searchedCity = MutableLiveData("")

    fun setGpsStatus(value: Boolean) {
        isGpsEnabled.postValue(value)
    }

    fun setNetworkAvailable(value: Boolean) {
        isNetworkAvailable.postValue(value)
    }

    fun setSearchedCity(city:String){
        searchedCity.postValue(city)
    }

    val readyToFetch = MediatorLiveData<Pair<Boolean?, Boolean?>>().apply {
        addSource(isGpsEnabled) {
            value = Pair(it, isNetworkAvailable.value)
        }
        addSource(isNetworkAvailable) {
            value = Pair(isGpsEnabled.value, it)
        }
    }

    fun getWeather(city:String?=null) = repo.getWeather(city).asLiveData()
}