package com.baymax.weatherforecast.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.baymax.weatherforecast.api.google_place_api.Location
import com.baymax.weatherforecast.api.weather_api.domain_model.WeatherDM
import com.baymax.weatherforecast.data.Result
import com.baymax.weatherforecast.data.UiState
import com.baymax.weatherforecast.data.WeatherRepository
import com.baymax.weatherforecast.utils.locationFlow
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeFragmentViewModel @Inject constructor(
    private val repo: WeatherRepository,
    private val locationClient: FusedLocationProviderClient
) : BaseViewModel() {
    var searchedCity = MutableLiveData("")
    val searchText = MutableLiveData<String>()
    val mutableLocation = MutableLiveData<Location>(null)
    var placeIdMap = mutableMapOf<String, String>()

    private val _weatherData = MutableLiveData<UiState>(UiState.Empty)
    val weatherData: LiveData<UiState> get() = _weatherData

    private val _weatherDetailsList = MutableLiveData<List<WeatherDM>>()
    val weatherDetailsList: LiveData<List<WeatherDM>> = _weatherDetailsList

    fun setUiState(state: UiState) {
        _weatherData.value = state
    }

    fun setSearchedCity(city: String) {
        searchedCity.postValue(city)
    }

    fun setWeatherDetailsList(list: List<WeatherDM>) {
        _weatherDetailsList.value = list
    }

    suspend fun getWeather(searchedLocation: Location) {
        _weatherData.value = UiState.Loading("Fetching Weather")
        when (val data = withContext(Dispatchers.IO) { repo.getWeather(searchedLocation) }) {
            is Result.Failure -> data.msg?.let {
                UiState.Error(it)
            } ?: UiState.Error("Something went wrong!")
            is Result.Success -> UiState.Success(data.data)
        }.also { _weatherData.value = it }
    }


    fun startCollectingDeviceLocation() = viewModelScope.launch {
        locationClient.locationFlow().collectLatest { location ->
            getWeather(location)
        }
    }

    fun getSuggestions(searchText: String) = repo.getSuggestions(searchText).asLiveData()

    suspend fun getCoordinates(placeId: String) = repo.getCoordinates(placeId)
}