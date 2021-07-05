package com.baymax.weatherforcast.ui.fragments.home_fragment.ui

import androidx.lifecycle.*
import com.baymax.weatherforcast.ui.fragments.home_fragment.data.WeatherRepository

class HomeFramentViewModel(
    private val repo: WeatherRepository
) : ViewModel() {
    var isGpsEnabled = MutableLiveData<Boolean>()
    var isNetworkAvailable = MutableLiveData<Boolean>()
    var searchedCity = MutableLiveData("")
    var isPermissionGranted = MutableLiveData<Boolean>()

    fun setGpsStatus(value: Boolean) {
        isGpsEnabled.postValue(value)
    }

    fun setNetworkAvailable(value: Boolean) {
        isNetworkAvailable.postValue(value)
    }

    fun setSearchedCity(city: String) {
        searchedCity.postValue(city)
    }

    fun setPermissionGranted(value: Boolean) {
        isPermissionGranted.postValue(value)
    }

    val readyToFetch = MediatorLiveData<Map<String,Boolean>>().apply {
        addSource(isGpsEnabled) { gps ->
            value = isNetworkAvailable.value?.let { network ->
                isPermissionGranted.value?.let { permission ->
                    mapOf(
                        "permission" to permission,
                        "network" to network,
                        "gps" to gps
                    )
                }
            }
        }
        addSource(isNetworkAvailable) { network ->
            value = isGpsEnabled.value?.let { gps ->
                isPermissionGranted.value?.let { permission ->
                    mapOf(
                        "permission" to permission,
                        "network" to network,
                        "gps" to gps
                    )
                }
            }
        }
        addSource(isPermissionGranted) { permission ->
            value = isGpsEnabled.value?.let { gps ->
                isNetworkAvailable.value?.let { network ->
                    mapOf(
                        "permission" to permission,
                        "network" to network,
                        "gps" to gps
                    )
                }
            }
        }
    }

    fun getWeather(city: String? = null) = repo.getWeather(city).asLiveData()
}