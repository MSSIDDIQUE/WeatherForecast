package com.baymax.weather.forecast.weather_forecast.presentation.view_model

import androidx.lifecycle.viewModelScope
import com.baymax.weather.forecast.data.ResponseWrapper
import com.baymax.weather.forecast.fetch_location.api.data_transfer_model.Location
import com.baymax.weather.forecast.fetch_location.data.FetchCurrentDeviceLocationUseCase
import com.baymax.weather.forecast.fetch_location.data.FetchLocationPredictionsUseCase
import com.baymax.weather.forecast.presentation.view_models.BaseViewModel
import com.baymax.weather.forecast.presentation.view_state.BaseViewState
import com.baymax.weather.forecast.presentation.view_state.SnackBarViewState
import com.baymax.weather.forecast.weather_forecast.api.domain_model.ApiResponseDM
import com.baymax.weather.forecast.weather_forecast.data.FetchWeatherUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeFragmentViewModel @Inject constructor(
    private val fetchWeatherUseCase: FetchWeatherUseCase,
    private val fetchLocationPredictionsUseCase: FetchLocationPredictionsUseCase,
    private val fetchCurrentDeviceLocationUseCase: FetchCurrentDeviceLocationUseCase
) : BaseViewModel() {

    private val locationState = MutableStateFlow(Location(0.0, 0.0))

    private val _weatherState = MutableStateFlow<BaseViewState<ApiResponseDM>>(BaseViewState.Empty)
    val weatherState: StateFlow<BaseViewState<ApiResponseDM>> = _weatherState

    private val _predictionsState = MutableStateFlow<Map<String, Location>>(emptyMap())
    val predictionsState: StateFlow<Map<String, Location>> = _predictionsState

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
        fetchCurrentDeviceLocationUseCase().collectLatest { response ->
            _weatherState.value = BaseViewState.Loading("Fetching current location")
            when (response) {
                is ResponseWrapper.Failure -> {
                    _weatherState.value = BaseViewState.Error(response.msg ?: "Unable to fetch device location")
                }

                is ResponseWrapper.Success -> {
                    locationState.value = response.data
                    fetchWeatherForLocation()
                }
            }
        }
    }

    fun updateLocationOnSearch(location: Location) = viewModelScope.launch {
        locationState.value = location
        fetchWeatherForLocation()
    }

    @OptIn(FlowPreview::class)
    fun fetchAndUpdatePredictionsList(searchText: String) = viewModelScope.launch {
        fetchLocationPredictionsUseCase(searchText).debounce(500)
            .collectLatest { response ->
                when (response) {
                    is ResponseWrapper.Failure -> setSnackBarViewState(
                        SnackBarViewState.Error(response.msg ?: "Unable to fetch predictions")
                    )

                    is ResponseWrapper.Success -> response?.data?.let { predictions ->
                        _predictionsState.value = predictions
                    } ?: setSnackBarViewState(SnackBarViewState.Error("No such location exists"))
                }
            }
    }
}
