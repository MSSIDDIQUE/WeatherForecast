package com.baymax.weatherforcast.ui.fragments.home_fragment.ui

import androidx.lifecycle.*
import com.baymax.weatherforcast.api.response.googlePlaceApi.Location
import com.baymax.weatherforcast.ui.fragments.home_fragment.data.WeatherRepository
import javax.inject.Inject

class HomeFragmentViewModel @Inject constructor(
    private val repo: WeatherRepository
) : ViewModel() {
    var isGpsEnabled = MutableLiveData<Boolean>()
    var isNetworkAvailable = MutableLiveData<Boolean>()
    var searchedCity = MutableLiveData("")
    var isPermissionGranted = MutableLiveData<Boolean>()
    val searchText = MutableLiveData<String>()
    val mutableLocation = MutableLiveData<Location>(null)
    var placeIdMap = mutableMapOf<String, String>()

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

    // TODO: this piece of code can be optimized further
    val readyToFetch = MediatorLiveData<Map<String, Boolean>>().apply {
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

    fun getWeather(searchedLocation: Location? = null) = repo.getWeather(searchedLocation).asLiveData()

    fun getSuggestions(searchText:String) = repo.getSuggestions(searchText).asLiveData()

    suspend fun getCoordinates(placeId:String) = repo.getCoordinates(placeId)
}