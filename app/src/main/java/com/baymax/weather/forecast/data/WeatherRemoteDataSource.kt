package com.baymax.weather.forecast.data

import android.util.Log
import com.baymax.weather.forecast.api.GooglePlaceApiService
import com.baymax.weather.forecast.api.WeatherApiService
import com.baymax.weather.forecast.api.google_place_api.Location
import com.baymax.weather.forecast.utils.PrefHelper
import com.baymax.weather.forecast.utils.get

class WeatherRemoteDataSource(
    private val weatherApiService: WeatherApiService,
    private val googlePlaceApiService: GooglePlaceApiService,
    private val prefHelper: PrefHelper
) : BaseDataSource() {

    suspend fun fetchWeatherForLocation(
        location: Location
    ) = getResult {
        weatherApiService.getWeatherOfCity(
            location.lat.toString(),
            location.lng.toString(),
            prefHelper.sharedPref[PrefHelper.WEATHER_API_KEY, ""]
        )
    }

    suspend fun fetchPredictions(
        searchText: String
    ) = getResult {
        Log.d("Saquib", " the api is being called for ${searchText}")
        googlePlaceApiService.getPredictions(
            searchText,
            prefHelper.sharedPref[PrefHelper.GOOGLE_PLACE_API_KEY, ""]
        )
    }

    suspend fun fetchCoordinates(
        placeId: String
    ) = getResult {
        Log.d("Saquib", " the api is being called for ${placeId}")
        googlePlaceApiService.getCoordinates(
            placeId,
            prefHelper.sharedPref[PrefHelper.GOOGLE_PLACE_API_KEY, ""]
        )
    }
}