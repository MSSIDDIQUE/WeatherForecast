package com.baymax.weather.forecast.fetch_location.presentation.model

sealed interface PredictionsState {
    data class Matches(val list: List<PredictionDAO>) : PredictionsState
    data object NoMatch : PredictionsState
    data class Error(val errorMessage: String? = null): PredictionsState
}