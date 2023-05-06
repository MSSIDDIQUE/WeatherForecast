package com.baymax.weather.forecast.fetch_location.data

import com.baymax.weather.forecast.data.ResponseWrapper
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchPredictionsUseCase @Inject constructor(private val repo: FetchLocationRepository) {
    operator fun invoke(text: String) = flow {
        when (val predictionResponse = repo.getSuggestions(text)) {
            is ResponseWrapper.Success -> ResponseWrapper.Success(predictionResponse.data)
            is ResponseWrapper.Failure -> ResponseWrapper.Failure(predictionResponse.msg)
            else -> ResponseWrapper.Empty
        }.also { emit(it) }
    }
}
