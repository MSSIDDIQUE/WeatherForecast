package com.baymax.weather.forecast.fetch_location.api.mappers

import com.baymax.weather.forecast.fetch_location.api.model.PlaceIdSuccessResponseDTO
import com.baymax.weather.forecast.fetch_location.api.model.PredictionsSuccessResponseDTO
import com.baymax.weather.forecast.fetch_location.presentation.model.CoordinatesDAO
import com.baymax.weather.forecast.fetch_location.presentation.model.PredictionDAO

object GooglePlacesDataMapper {

    val toPredictionsDAO: (PredictionsSuccessResponseDTO) -> List<PredictionDAO> = { responseDTO ->
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

    val toCoordinatesDAO: (PlaceIdSuccessResponseDTO) -> CoordinatesDAO = { responseDTO ->
        CoordinatesDAO(
            lat = responseDTO.result?.geometry?.location?.lat ?: 0.0,
            lng = responseDTO.result?.geometry?.location?.lng ?: 0.0,
        )
    }
}
