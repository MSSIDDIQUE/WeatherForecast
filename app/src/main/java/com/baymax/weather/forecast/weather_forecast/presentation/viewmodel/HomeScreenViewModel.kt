package com.baymax.weather.forecast.weather_forecast.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baymax.weather.forecast.fetch_location.data.CacheLocationUseCase
import com.baymax.weather.forecast.fetch_location.data.FetchCoordinatesUseCase
import com.baymax.weather.forecast.fetch_location.data.FetchPredictionsUseCase
import com.baymax.weather.forecast.fetch_location.presentation.model.CoordinatesDAO
import com.baymax.weather.forecast.fetch_location.presentation.model.LocationCoordinatesState
import com.baymax.weather.forecast.fetch_location.presentation.model.PredictionsState
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
    private val fetchCoordinatesUseCase: FetchCoordinatesUseCase,
    private val fetchPredictionsUseCase: FetchPredictionsUseCase
) : ViewModel() {

    private val location = MutableStateFlow(CoordinatesDAO(0.0, 0.0))

    val snackBarState = MutableStateFlow<SnackBarViewState>(SnackBarViewState.Hidden)

    val searchQuery = MutableStateFlow("")

    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun setSnackBarState(state: SnackBarViewState) {
        snackBarState.value = state
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val predictions = searchQuery.debounce(1000).filter {
        if (it.length < 3) return@filter false else true
    }.transformLatest { searchQuery ->
        fetchPredictionsUseCase(searchQuery).collectLatest { response ->
            when (response) {
                is PredictionsState.Matches -> response.list
                else -> emptyList()
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
        emit(fetchWeatherUseCase(lat, lng))
    }.distinctUntilChanged().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(25000L),
        initialValue = WeatherReportsState.Idle,
    )

    fun updateCachedLocation() = viewModelScope.launch {
        location.value = fetchCoordinatesUseCase.fetchLocationFromCache()
    }

    fun updateCurrentLocation() = viewModelScope.launch {
        fetchCoordinatesUseCase.fetchLocationFromGps().collectLatest { result ->
            when(result){
                is LocationCoordinatesState.Error -> setSnackBarState(
                    SnackBarViewState.Show(SnackBarData(result.errorMessage ?: "Unable to fetch device location"))
                )
                is LocationCoordinatesState.Found -> result.coordinatesDao.let { (latestLat, latestLng) ->
                    val (cachedLat, cachedLng) = withContext(Dispatchers.Default) {
                        fetchCoordinatesUseCase.fetchLocationFromCache()
                    }
                    if (latestLat != cachedLat || latestLng != cachedLng) {
                        location.value = result.coordinatesDao
                        cacheLocationUseCase.cacheInSharedPrefs(result.coordinatesDao)
                    } else {
                        setSnackBarState(
                            SnackBarViewState.Show(SnackBarData("No change in device location"))
                        )
                    }
                }
                LocationCoordinatesState.NotFound -> TODO()
            }
        }
    }

    fun updateLocationFromPlaceId(placeId: String) = viewModelScope.launch {
        when (val response = fetchCoordinatesUseCase.fetchLocationFromPlaceId(placeId)) {
            is LocationCoordinatesState.Found -> location.value = response.coordinatesDao
            else -> {}
        }
    }

    suspend fun isLastLocationCached() = cacheLocationUseCase.isLastLocationCached()
}
