package com.baymax.weather.forecast.weather_forecast.presentation.view_model

import androidx.lifecycle.viewModelScope
import com.baymax.weather.forecast.data.ResponseWrapper
import com.baymax.weather.forecast.fetch_location.api.data_transfer_model.Location
import com.baymax.weather.forecast.fetch_location.data.CacheLocationUseCase
import com.baymax.weather.forecast.fetch_location.data.FetchLocationUseCase
import com.baymax.weather.forecast.fetch_location.data.FetchPredictionsUseCase
import com.baymax.weather.forecast.presentation.view_models.BaseViewModel
import com.baymax.weather.forecast.presentation.view_state.BaseViewState
import com.baymax.weather.forecast.presentation.view_state.ProgressBarViewState
import com.baymax.weather.forecast.presentation.view_state.SnackBarViewState
import com.baymax.weather.forecast.weather_forecast.data.FetchWeatherUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeFragmentViewModel @Inject constructor(
    private val fetchWeatherUseCase: FetchWeatherUseCase,
    private val cacheLocationUseCase: CacheLocationUseCase,
    private val fetchLocationUseCase: FetchLocationUseCase,
    private val fetchPredictionsUseCase: FetchPredictionsUseCase
) : BaseViewModel() {
    private val location = MutableStateFlow(Location(0.0, 0.0))

    val searchQuery = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val predictions = searchQuery.debounce(1000).filter {
        if (it.length < 3) return@filter false else true
    }.transformLatest { searchQuery ->
        fetchPredictionsUseCase(searchQuery).collectLatest { response ->
            when (response) {
                is ResponseWrapper.Failure -> emptyMap()
                is ResponseWrapper.Success -> response.data
            }.also { emit(it) }
        }
    }.distinctUntilChanged().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyMap()
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val weatherState = location.filter { (lat, lng) ->
        if (lat == 0.0 && lng == 0.0) return@filter false else true
    }.transformLatest { (lat, lng) ->
        emit(BaseViewState.Loading("Fetching Weather"))
        fetchWeatherUseCase(lat, lng).collectLatest { weatherState ->
            when (weatherState) {
                is ResponseWrapper.Failure -> weatherState.msg?.let {
                    BaseViewState.Error(it)
                } ?: BaseViewState.Error("Something went wrong!")

                is ResponseWrapper.Success -> BaseViewState.Success(weatherState.data)
            }.also {
                emit(it)
            }
        }
    }.distinctUntilChanged().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = BaseViewState.Empty
    )

    fun updateLastLocation() = viewModelScope.launch {
        when {
            cacheLocationUseCase.isLastLocationCached() -> fetchLocationUseCase.fetchLocationFromCache()
                .collectLatest { cachedLocation ->
                    setProgressBarState(ProgressBarViewState.Show("Fetching current location"))
                    if (cachedLocation != null) {
                        location.value = cachedLocation
                        setProgressBarState(ProgressBarViewState.Hide)
                    } else {
                        updateCurrentLocation()
                    }
                }

            else -> updateCurrentLocation()
        }
    }

    fun updateCurrentLocation() = viewModelScope.launch {
        fetchLocationUseCase.fetchLocationFromGps().collectLatest { response ->
            when (response) {
                is ResponseWrapper.Failure -> setSnackBarState(
                    SnackBarViewState.Error(response.msg ?: "Unable to fetch device location")
                )

                is ResponseWrapper.Success -> {
                    location.value = response.data
                    cacheLocationUseCase.cacheInSharedPrefs(response.data)
                }
            }.also { setProgressBarState(ProgressBarViewState.Hide) }
        }
    }

    fun updateLocationFromPlaceId(placeId: String) = viewModelScope.launch {
        when (val response = fetchLocationUseCase.fetchLocationFromPlaceId(placeId)) {
            is ResponseWrapper.Failure -> SnackBarViewState.Error(
                response.msg ?: "Unable to fetch location"
            )

            is ResponseWrapper.Success -> location.value = response.data.result.geometry.location
        }
    }

    suspend fun isLastLocationCached() = cacheLocationUseCase.isLastLocationCached()
}
