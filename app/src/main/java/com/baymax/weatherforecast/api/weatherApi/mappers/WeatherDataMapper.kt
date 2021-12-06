package com.baymax.weatherforecast.api.weatherApi.mappers

import com.baymax.weatherforecast.api.weatherApi.dataTransferModel.ApiResponseDTO
import com.baymax.weatherforecast.api.weatherApi.domainModel.ApiResponseDM
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