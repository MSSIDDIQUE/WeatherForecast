package com.baymax.weather.forecast.weather_forecast.api

import com.baymax.weather.forecast.data.ApiResponse
import com.baymax.weather.forecast.utils.PrefHelper
import com.baymax.weather.forecast.utils.safeRequest
import com.baymax.weather.forecast.weather_forecast.api.model.ApiResponseDTO
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.http.HttpMethod
import io.ktor.http.path

class WeatherApiServiceImpl(private val client: HttpClient) : WeatherApiService {
    override suspend fun getWeatherOfCity(
        latitude: String,
        longitude: String,
    ): ApiResponse<ApiResponseDTO, ApiResponseDTO> = client.safeRequest {
        url {
            method = HttpMethod.Get
            path("forecast")
        }
        parameter(PrefHelper.LAT, latitude)
        parameter(PrefHelper.LNG, longitude)
    }
}
