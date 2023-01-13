package com.baymax.weather.forecast.usecases.weather_forecast.presentation.view_model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.baymax.weather.forecast.data.BaseViewState
import com.baymax.weather.forecast.data.ResponseWrapper
import com.baymax.weather.forecast.presentation.view_models.BaseViewModel
import com.baymax.weather.forecast.usecases.search_location.data.SearchLocationRepository
import com.baymax.weather.forecast.usecases.search_location.data.data_transfer_model.Location
import com.baymax.weather.forecast.usecases.weather_forecast.data.WeatherRepository
import com.baymax.weather.forecast.usecases.weather_forecast.data.domain_model.ApiResponseDM
import com.baymax.weather.forecast.usecases.weather_forecast.data.domain_model.WeatherDM
import com.baymax.weather.forecast.utils.LocationUtils.locationFlow
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeFragmentViewModel @Inject constructor(
    private val weatherRepo: WeatherRepository,
    private val appContext: Application,
    private val searchLocationRepo: SearchLocationRepository,
    private val locationClient: FusedLocationProviderClient
) : BaseViewModel() {
    private val searchChanel = ConflatedBroadcastChannel<String>()
    var placeIdMap = mutableMapOf<String, String>()

    private val _weatherData = MutableLiveData<BaseViewState<ApiResponseDM>>(BaseViewState.Empty)
    val weatherData: LiveData<BaseViewState<ApiResponseDM>> = _weatherData

    private val _weatherDetailsList = MutableLiveData<List<WeatherDM>>()
    val weatherDetailsList: LiveData<List<WeatherDM>> = _weatherDetailsList

    val suggestionsLiveData = searchChanel.asFlow()
        .flatMapLatest { text ->
            searchLocationRepo.getSuggestions(text)
        }.map { result ->
            when (result) {
                is ResponseWrapper.Failure -> result.msg?.let {
                    BaseViewState.Error(it)
                } ?: BaseViewState.Error("Something went wrong!")

                is ResponseWrapper.Success -> BaseViewState.Success(result.data)
            }
        }.catch { cause: Throwable ->
            cause.printStackTrace()
        }.asLiveData()

    fun setSearchQuery(search: String) {
        searchChanel.trySend(search).isSuccess
    }

    fun setUiState(state: BaseViewState<ApiResponseDM>) {
        _weatherData.value = state
    }

    suspend fun fetchWeatherForLocation(searchedLocation: Location) {
        _weatherData.value = BaseViewState.Loading("Fetching Weather")
        when (val result = withContext(Dispatchers.IO) { weatherRepo.getWeather(searchedLocation) }) {
            is ResponseWrapper.Failure -> result.msg?.let {
                BaseViewState.Error(it)
            } ?: BaseViewState.Error("Something went wrong!")

            is ResponseWrapper.Success -> BaseViewState.Success(result.data)
        }.also {
            _weatherData.value = it
        }
    }

    fun startCollectingDeviceLocationAndFetchWeather() = viewModelScope.launch {
        setUiState(BaseViewState.Loading("Collecting Location"))
        locationClient.locationFlow(appContext).collectLatest { location ->
            fetchWeatherForLocation(location)
        }
    }

    suspend fun getCoordinates(placeId: String) = searchLocationRepo.getCoordinates(placeId)
}
