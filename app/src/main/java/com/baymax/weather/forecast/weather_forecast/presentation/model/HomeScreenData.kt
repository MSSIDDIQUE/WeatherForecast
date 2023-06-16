package com.baymax.weather.forecast.weather_forecast.presentation.model

import com.baymax.weather.forecast.fetch_location.presentation.model.PredictionDAO
import com.baymax.weather.forecast.presentation.view_state.SnackBarViewState
import com.baymax.weather.forecast.weather_forecast.presentation.view_state.WeatherReportsState

data class HomeScreenData(
    val searchQueryState: String,
    val snackBarState: SnackBarViewState,
    val weatherState: WeatherReportsState,
    val predictionsState: List<PredictionDAO>,
    val onCurrentLocationClick: () -> Unit,
    val onPredictionClick: (String) -> Unit,
    val onSearchQueryUpdate: (String) -> Unit
)