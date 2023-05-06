package com.baymax.weather.forecast.fetch_location.api.mappers

import com.baymax.weather.forecast.fetch_location.api.model.PlaceIdResponseDTO
import com.baymax.weather.forecast.fetch_location.api.model.PredictionsResponseDTO
import com.baymax.weather.forecast.fetch_location.presentation.model.LocationDAO
import com.baymax.weather.forecast.fetch_location.presentation.model.PredictionDAO

object GooglePlacesDataMapper {

    val toPredictionsDAO: (PredictionsResponseDTO) -> List<PredictionDAO> = { responseDTO ->
        mutableListOf<PredictionDAO>().apply {
            responseDTO.predictions?.map { prediction ->
                add(
                    PredictionDAO(
                        placeId = prediction.placeId.orEmpty(),
                        description = prediction.description.orEmpty(),
                    ),
                )
            }
        }
    }

    val toLocationDAO: (PlaceIdResponseDTO) -> LocationDAO = { responseDTO ->
        LocationDAO(
            lat = responseDTO.result?.geometry?.location?.lat ?: 0.0,
            lng = responseDTO.result?.geometry?.location?.lng ?: 0.0,
        )
    }
}
