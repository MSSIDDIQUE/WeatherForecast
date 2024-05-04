package com.baymax.weather.forecast.fetch_location.presentation.model

sealed interface LocationCoordinatesState {
    data class Found(val coordinatesDao: CoordinatesDAO) : LocationCoordinatesState
    data object NotFound : LocationCoordinatesState
    data class Error(val errorMessage: String? = null) : LocationCoordinatesState
}