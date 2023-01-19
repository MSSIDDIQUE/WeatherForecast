package com.baymax.weather.forecast.fetch_location.data

import com.baymax.weather.forecast.data.ResponseWrapper
import com.baymax.weather.forecast.fetch_location.api.data_transfer_model.Location
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchPredictionsUseCase @Inject constructor(private val repo: FetchLocationRepository) {
    operator fun invoke(text: String) = flow {
        emit(
            when (val predictionResponse = repo.getSuggestions(text)) {
                is ResponseWrapper.Failure -> ResponseWrapper.Failure(predictionResponse.msg)
                is ResponseWrapper.Success -> ResponseWrapper.Success(
                    predictionResponse.data.predictions?.let { predictions ->
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
                )
            }
        )
    }
}
