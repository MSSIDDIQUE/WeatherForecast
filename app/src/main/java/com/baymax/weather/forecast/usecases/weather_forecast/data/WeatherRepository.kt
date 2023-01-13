package com.baymax.weather.forecast.usecases.weather_forecast.data

import com.baymax.weather.forecast.data.ResponseWrapper
import com.baymax.weather.forecast.usecases.search_location.data.data_transfer_model.Location
import com.baymax.weather.forecast.usecases.weather_forecast.data.domain_model.ApiResponseDM

interface WeatherRepository {
    suspend fun getWeather(location: Location): ResponseWrapper<ApiResponseDM>
}
