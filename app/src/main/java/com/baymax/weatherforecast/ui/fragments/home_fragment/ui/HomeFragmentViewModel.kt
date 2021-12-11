package com.baymax.weatherforecast.ui.fragments.home_fragment.ui

import androidx.lifecycle.*
import com.baymax.weatherforecast.api.googlePlaceApi.Location
import com.baymax.weatherforecast.api.weatherApi.domainModel.ApiResponseDM
import com.baymax.weatherforecast.api.weatherApi.domainModel.WeatherDM
import com.baymax.weatherforecast.data.Result
import com.baymax.weatherforecast.ui.fragments.home_fragment.data.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeFragmentViewModel @Inject constructor(
    private val repo: WeatherRepository
) : ViewModel() {
    var searchedCity = MutableLiveData("")
    val searchText = MutableLiveData<String>()
    val mutableLocation = MutableLiveData<Location>(null)
    var placeIdMap = mutableMapOf<String, String>()

    private val _weatherData = MutableStateFlow<UiState>(UiState.Empty)
    val weatherData: StateFlow<UiState> = _weatherData

    private val _weatherDetailsList = MutableLiveData<List<WeatherDM>>()
    val weatherDetailsList: LiveData<List<WeatherDM>> = _weatherDetailsList

    fun setSearchedCity(city: String) {
        searchedCity.postValue(city)
    }

    fun setWeatherDetailsList(list: List<WeatherDM>){
        _weatherDetailsList.value = list
    }

    fun getWeather(searchedLocation: Location) = viewModelScope.launch {
        _weatherData.value = UiState.Loading
        when (val data = withContext(Dispatchers.IO) { repo.getWeather(searchedLocation) }) {
            is Result.Failure -> {
                _weatherData.value = data.msg?.let {
                    UiState.Error(it)
                } ?: UiState.Error("Something went wrong!")
            }
            is Result.Success -> {
                data.data.let {
                    _weatherData.value = UiState.Success(it)
                } ?: run {
                    _weatherData.value = UiState.Empty
                }
            }
        }
    }

    fun getSuggestions(searchText: String) = repo.getSuggestions(searchText).asLiveData()

    suspend fun getCoordinates(placeId: String) = repo.getCoordinates(placeId)

    sealed class UiState {
        data class Success(val wData: ApiResponseDM) : UiState()
        data class Error(val msg: String) : UiState()
        object Loading : UiState()
        object Empty : UiState()
    }
}