package com.baymax.weatherforecast.ui.view_model

import androidx.lifecycle.*
import com.baymax.weatherforecast.api.google_place_api.Location
import com.baymax.weatherforecast.api.weather_api.domain_model.ApiResponseDM
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

    private val _weatherData = MutableLiveData<UiState<ApiResponseDM>>(UiState.Empty)
    val weatherData: LiveData<UiState<ApiResponseDM>> = _weatherData

    private val _weatherDetailsList = MutableLiveData<List<WeatherDM>>()
    val weatherDetailsList: LiveData<List<WeatherDM>> = _weatherDetailsList

    fun setUiState(state: UiState<ApiResponseDM>) {
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
        when (val result = withContext(Dispatchers.IO) { repo.getWeather(searchedLocation) }) {
            is Result.Failure -> result.msg?.let {
                UiState.Error(it)
            } ?: UiState.Error("Something went wrong!")
            is Result.Success -> UiState.Success(result.data)
        }.also {
            _weatherData.value = it
        }
    }


    fun startCollectingDeviceLocationAndFetchWeather() = viewModelScope.launch {
        setUiState(UiState.Loading("Collecting Location"))
        locationClient.locationFlow().collectLatest { location ->
            getWeather(location)
        }
    }

    fun getSuggestions(searchText: String) = repo.getSuggestions(searchText).asLiveData()

    suspend fun getCoordinates(placeId: String) = repo.getCoordinates(placeId)
}