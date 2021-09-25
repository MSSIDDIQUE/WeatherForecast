package com.baymax.weatherforcast.ui.fragments.home_fragment.data

import android.util.Log
import com.baymax.weatherforcast.api.BaseDataSource
import com.baymax.weatherforcast.api.GooglePlaceApiService
import com.baymax.weatherforcast.api.WeatherApiService
import com.baymax.weatherforcast.utils.PrefHelper
import com.baymax.weatherforcast.utils.get

class WeatherRemoteDataSource(
    private val weatherApiService: WeatherApiService,
    private val googlePlaceApiService: GooglePlaceApiService,
    private val prefHelper: PrefHelper
) : BaseDataSource() {

    suspend fun fetchWeatherForLocation(
        latitude:String,
        longitude:String
    ) = getResult {
        weatherApiService.getWeatherOfCity(
            latitude,
            longitude,
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