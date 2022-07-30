package com.baymax.weather.forecast.api.weather_api.mappers

import com.baymax.weather.forecast.api.weather_api.data_transfer_model.ApiResponseDTO
import com.baymax.weather.forecast.api.weather_api.domain_model.ApiResponseDM
import com.baymax.weather.forecast.utils.mapApiResponseDTO
import com.baymax.weather.forecast.utils.mapNullInputList
import com.baymax.weather.forecast.utils.mapWeatherDTO
import com.baymax.weather.forecast.utils.mapWeatherSummaryDTO

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