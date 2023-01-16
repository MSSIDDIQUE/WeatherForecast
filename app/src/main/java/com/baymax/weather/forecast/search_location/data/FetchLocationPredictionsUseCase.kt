package com.baymax.weather.forecast.search_location.data

import com.baymax.weather.forecast.data.ResponseWrapper
import com.baymax.weather.forecast.search_location.api.data_transfer_model.Location
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchLocationPredictionsUseCase @Inject constructor(private val repo: SearchLocationRepository) {
    operator fun invoke(text: String) = flow {
        emit(
            when (val predictionResponse = repo.getSuggestions(text)) {
                is ResponseWrapper.Failure -> emptyMap()
                is ResponseWrapper.Success -> predictionResponse.data.predictions?.let { predictions ->
                    mutableMapOf<String, Location>().apply {
                        predictions.forEach { prediction ->
                            when (val locationResponse = repo.getCoordinates(prediction.placeId)) {
                                is ResponseWrapper.Failure -> {}
                                is ResponseWrapper.Success -> put(
                                    prediction.description,
                                    locationResponse.data.result.geometry.location
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}
