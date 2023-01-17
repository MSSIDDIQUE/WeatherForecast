package com.baymax.weather.forecast.weather_forecast.api.mappers

import com.baymax.weather.forecast.utils.mapApiResponseDTO
import com.baymax.weather.forecast.utils.mapNullInputList
import com.baymax.weather.forecast.utils.mapWeatherDTO
import com.baymax.weather.forecast.utils.mapWeatherSummaryDTO
import com.baymax.weather.forecast.weather_forecast.api.data_transfer_model.ApiResponseDTO
import com.baymax.weather.forecast.weather_forecast.api.domain_model.ApiResponseDM

object WeatherDataMapper {

    fun apiResponseMapper(): (ApiResponseDTO) -> ApiResponseDM = { apiResponseDTO ->
        mapApiResponseDTO(apiResponseDTO) { weatherList ->
            mapNullInputList(weatherList) { weatherDTO ->
                mapWeatherDTO(weatherDTO) { listWeatherSummaryDTO ->
                    mapNullInputList(listWeatherSummaryDTO) { weatherSummaryDTO ->
                        mapWeatherSummaryDTO(weatherSummaryDTO)
                    }
                }
            }
        }
    }
}
