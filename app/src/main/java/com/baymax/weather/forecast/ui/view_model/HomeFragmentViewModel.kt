package com.baymax.weather.forecast.ui.view_model

import androidx.lifecycle.*
import com.baymax.weather.forecast.api.google_place_api.Location
import com.baymax.weather.forecast.api.weather_api.domain_model.ApiResponseDM
import com.baymax.weather.forecast.api.weather_api.domain_model.WeatherDM
import com.baymax.weather.forecast.data.Result
import com.baymax.weather.forecast.data.UiState
import com.baymax.weather.forecast.data.WeatherRepository
import com.baymax.weather.forecast.utils.LocationUtils.locationFlow
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeFragmentViewModel @Inject constructor(
    private val repo: WeatherRepository,
    private val locationClient: FusedLocationProviderClient
) : BaseViewModel() {
    private val searchedCity = MutableStateFlow("")
    private val searchChanel = ConflatedBroadcastChannel<String>()
    val mutableLocation = MutableLiveData<Location>(null)
    var placeIdMap = mutableMapOf<String, String>()

    private val _weatherData = MutableLiveData<UiState<ApiResponseDM>>(UiState.Empty)
    val weatherData: LiveData<UiState<ApiResponseDM>> = _weatherData

    private val _weatherDetailsList = MutableLiveData<List<WeatherDM>>()
    val weatherDetailsList: LiveData<List<WeatherDM>> = _weatherDetailsList

    val suggestionsLiveData = searchChanel.asFlow()
        .flatMapLatest { text ->
            repo.getSuggestions(text)
        }.map { result ->
            when (result) {
                is Result.Failure -> result.msg?.let {
                    UiState.Error(it)
                } ?: UiState.Error("Something went wrong!")
                is Result.Success -> UiState.Success(result.data)
            }
        }.catch { cause: Throwable ->
            cause.printStackTrace()
        }.asLiveData()

    fun setSearchQuery(search: String) {
        searchChanel.trySend(search).isSuccess
    }

    fun setUiState(state: UiState<ApiResponseDM>) {
        _weatherData.value = state
    }

    fun setSearchedCity(city: String) {
        searchedCity.value = city
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

    suspend fun getCoordinates(placeId: String) = repo.getCoordinates(placeId)
}