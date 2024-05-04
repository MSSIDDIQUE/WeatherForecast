package com.baymax.weather.forecast.fetch_location.data

import com.baymax.weather.forecast.data.ApiResponse
import com.baymax.weather.forecast.fetch_location.api.mappers.GooglePlacesDataMapper.toPredictionsDAO
import com.baymax.weather.forecast.fetch_location.presentation.model.PredictionsState
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchPredictionsUseCase @Inject constructor(private val repo: FetchLocationRepository) {
    operator fun invoke(text: String) = flow {
        when (val response = repo.getSuggestions(text)) {
            is ApiResponse.Error.GenericError -> PredictionsState.Error(response.errorMessage)
            is ApiResponse.Error.HttpError -> PredictionsState.Error(response.errorMessage)
            is ApiResponse.Error.SerializationError -> PredictionsState.Error(response.errorMessage)
            is ApiResponse.Success -> PredictionsState.Matches(toPredictionsDAO(response.body))
        }.also { emit(it) }
    }
}
