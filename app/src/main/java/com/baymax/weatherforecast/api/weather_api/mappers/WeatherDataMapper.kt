package com.baymax.weatherforecast.api.weather_api.mappers

import com.baymax.weatherforecast.api.weather_api.data_transfer_model.ApiResponseDTO
import com.baymax.weatherforecast.api.weather_api.domain_model.ApiResponseDM
import com.baymax.weatherforecast.utils.*

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