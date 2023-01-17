package com.baymax.weather.forecast.fetch_location.data

import com.baymax.weather.forecast.data.BaseDataSource
import com.baymax.weather.forecast.fetch_location.api.GooglePlaceApiService
import com.baymax.weather.forecast.utils.PrefHelper
import com.baymax.weather.forecast.utils.get
import javax.inject.Inject

class SearchLocationRemoteDataSource @Inject constructor(
    private val placesApiService: GooglePlaceApiService,
    private val prefHelper: PrefHelper
) : BaseDataSource() {

    suspend fun fetchPredictions(
        searchText: String
    ) = getResult {
        placesApiService.getPredictions(
            searchText,
            prefHelper.sharedPref[PrefHelper.GOOGLE_PLACE_API_KEY, ""]
        )
    }

    suspend fun fetchCoordinates(
        placeId: String
    ) = getResult {
        placesApiService.getCoordinates(
            placeId,
            prefHelper.sharedPref[PrefHelper.GOOGLE_PLACE_API_KEY, ""]
        )
    }
}
