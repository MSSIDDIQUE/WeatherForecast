package com.baymax.weatherforecast.ui.fragments.home_fragment.data

import android.util.Log
import com.baymax.weatherforecast.api.BaseDataSource
import com.baymax.weatherforecast.api.GooglePlaceApiService
import com.baymax.weatherforecast.api.WeatherApiService
import com.baymax.weatherforecast.api.googlePlaceApi.Location
import com.baymax.weatherforecast.utils.PrefHelper
import com.baymax.weatherforecast.utils.get

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