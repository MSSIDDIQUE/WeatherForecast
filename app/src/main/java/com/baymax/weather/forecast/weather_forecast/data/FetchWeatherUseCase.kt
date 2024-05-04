package com.baymax.weather.forecast.weather_forecast.data

import com.baymax.weather.forecast.data.ApiResponse
import com.baymax.weather.forecast.weather_forecast.api.mappers.WeatherDataMapper
import com.baymax.weather.forecast.weather_forecast.presentation.view_state.WeatherReportsState
import javax.inject.Inject

class FetchWeatherUseCase @Inject constructor(private val repo: WeatherRepository) {
    suspend operator fun invoke(lat: Double, lng: Double) =
        when (val result = repo.fetchWeather(lat, lng)) {
            is ApiResponse.Error.GenericError -> WeatherReportsState.Error("Something went wrong")
            is ApiResponse.Error.HttpError, is ApiResponse.Error.SerializationError -> WeatherReportsState.Error("Bad request")
            is ApiResponse.Success -> WeatherReportsState.Success(WeatherDataMapper.apiResponseMapper(result.body))
        }
}
