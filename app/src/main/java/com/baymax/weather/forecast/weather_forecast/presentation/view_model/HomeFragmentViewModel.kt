package com.baymax.weather.forecast.weather_forecast.presentation.view_model

import androidx.lifecycle.viewModelScope
import com.baymax.weather.forecast.data.BaseViewState
import com.baymax.weather.forecast.data.ResponseWrapper
import com.baymax.weather.forecast.presentation.view_models.BaseViewModel
import com.baymax.weather.forecast.search_location.api.data_transfer_model.Location
import com.baymax.weather.forecast.search_location.data.FetchCurrentDeviceLocationUseCase
import com.baymax.weather.forecast.search_location.data.FetchSuggestionsUseCase
import com.baymax.weather.forecast.weather_forecast.api.domain_model.ApiResponseDM
import com.baymax.weather.forecast.weather_forecast.data.FetchWeatherUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeFragmentViewModel @Inject constructor(
    private val fetchWeatherUseCase: FetchWeatherUseCase,
    private val fetchSuggestionsUseCase: FetchSuggestionsUseCase,
    private val fetchCurrentDeviceLocationUseCase: FetchCurrentDeviceLocationUseCase
) : BaseViewModel() {

    private val _locationState = MutableStateFlow(Location(0.0, 0.0))
    val locationState: StateFlow<Location> = _locationState

    private val _weatherState = MutableStateFlow<BaseViewState<ApiResponseDM>>(BaseViewState.Empty)
    val weatherState: StateFlow<BaseViewState<ApiResponseDM>> = _weatherState

    private val _predictionsState = MutableStateFlow<Map<String, Location>>(emptyMap())
    val predictionsState: StateFlow<Map<String, Location>> = _predictionsState

    fun setUiState(state: BaseViewState<ApiResponseDM>) {
        _weatherState.value = state
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun fetchWeatherForLocation() = locationState.flatMapLatest { (lat, lng) ->
        _weatherState.value = BaseViewState.Loading("Fetching Weather")
        fetchWeatherUseCase(lat, lng)
    }.collectLatest { data ->
        _weatherState.value = when (data) {
            is ResponseWrapper.Failure -> data.msg?.let {
                BaseViewState.Error(it)
            } ?: BaseViewState.Error("Something went wrong!")

            is ResponseWrapper.Success -> BaseViewState.Success(data.data)
        }
    }

    fun fetchAndUpdateDeviceLocation() = viewModelScope.launch {
        fetchCurrentDeviceLocationUseCase().collectLatest { loc ->
            _locationState.value = loc
            fetchWeatherForLocation()
        }
    }

    fun updateLocationOnSearch(location: Location) = viewModelScope.launch {
        _locationState.value = location
        fetchWeatherForLocation()
    }

    fun fetchAndUpdatePredictionsList(searchText: String) = viewModelScope.launch {
        fetchSuggestionsUseCase(searchText).collectLatest { predictions ->
            if (predictions != null) {
                _predictionsState.value = predictions
            }
        }
    }
}
