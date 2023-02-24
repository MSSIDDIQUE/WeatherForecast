package com.baymax.weather.forecast.weather_forecast.presentation.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baymax.weather.forecast.data.ResponseWrapper
import com.baymax.weather.forecast.fetch_location.data.CacheLocationUseCase
import com.baymax.weather.forecast.fetch_location.data.FetchLocationUseCase
import com.baymax.weather.forecast.fetch_location.data.FetchPredictionsUseCase
import com.baymax.weather.forecast.fetch_location.presentation.model.LocationDAO
import com.baymax.weather.forecast.presentation.model.SnackBarData
import com.baymax.weather.forecast.presentation.view_state.SnackBarViewState
import com.baymax.weather.forecast.weather_forecast.data.FetchWeatherUseCase
import com.baymax.weather.forecast.weather_forecast.presentation.view_state.WeatherReportsState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import javax.inject.Inject

class HomeScreenViewModel @Inject constructor(
    private val fetchWeatherUseCase: FetchWeatherUseCase,
    private val cacheLocationUseCase: CacheLocationUseCase,
    private val fetchLocationUseCase: FetchLocationUseCase,
    private val fetchPredictionsUseCase: FetchPredictionsUseCase,
) : ViewModel() {

    private val location = MutableStateFlow(LocationDAO(0.0, 0.0))

    val snackBarState = MutableStateFlow<SnackBarViewState>(SnackBarViewState.Hidden)

    val searchQuery = MutableStateFlow("")

    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun setSnackBarState(state: SnackBarViewState) {
        snackBarState.value = state
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val predictions = searchQuery.debounce(1000).filter {
        if (it.length < 3) return@filter false else true
    }.transformLatest { searchQuery ->
        fetchPredictionsUseCase(searchQuery).collectLatest { response ->
            when (response) {
                is ResponseWrapper.Failure -> emptyList()
                is ResponseWrapper.Success -> response.data
            }.also { emit(it) }
        }
    }.distinctUntilChanged().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList(),
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val weatherState = location.filter { (lat, lng) ->
        if (lat == 0.0 && lng == 0.0) return@filter false else true
    }.transformLatest { (lat, lng) ->
        emit(WeatherReportsState.Loading("Fetching Weather"))
        fetchWeatherUseCase(lat, lng).collectLatest { weatherState ->
            when (weatherState) {
                is ResponseWrapper.Failure -> weatherState.msg?.let {
                    WeatherReportsState.Error(it)
                } ?: WeatherReportsState.Error("Something went wrong!")

                is ResponseWrapper.Success -> WeatherReportsState.Success(weatherState.data)
            }.also {
                emit(it)
            }
        }
    }.distinctUntilChanged().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(25000L),
        initialValue = WeatherReportsState.Idle,
    )

    fun updateCachedLocation() = viewModelScope.launch {
        location.value = fetchLocationUseCase.fetchLocationFromCache()
    }

    fun updateCurrentLocation() = viewModelScope.launch {
        fetchLocationUseCase.fetchLocationFromGps().collectLatest { response ->
            when (response) {
                is ResponseWrapper.Failure -> setSnackBarState(
                    SnackBarViewState.Show(
                        SnackBarData(response.msg ?: "Unable to fetch device location")
                    )
                )

                is ResponseWrapper.Success -> response.data.let { (latestLat, latestLng) ->
                    val (cachedLat, cachedLng) = withContext(Dispatchers.Default) {
                        fetchLocationUseCase.fetchLocationFromCache()
                    }
                    if (latestLat != cachedLat && latestLng != cachedLng) {
                        location.value = response.data
                        cacheLocationUseCase.cacheInSharedPrefs(response.data)
                        setSnackBarState(
                            SnackBarViewState.Show(
                                SnackBarData("Device location updated successfully")
                            )
                        )
                    } else {
                        setSnackBarState(
                            SnackBarViewState.Show(
                                SnackBarData("No change in device location")
                            )
                        )
                    }
                }
            }
        }
    }

    fun updateLocationFromPlaceId(placeId: String) = viewModelScope.launch {
        when (val response = fetchLocationUseCase.fetchLocationFromPlaceId(placeId)) {
            is ResponseWrapper.Failure -> {}
            is ResponseWrapper.Success -> location.value = response.data
        }
    }

    suspend fun isLastLocationCached() = cacheLocationUseCase.isLastLocationCached()
}
