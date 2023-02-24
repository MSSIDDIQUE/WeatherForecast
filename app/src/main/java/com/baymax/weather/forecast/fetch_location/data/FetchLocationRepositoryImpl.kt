package com.baymax.weather.forecast.fetch_location.data

import com.baymax.weather.forecast.fetch_location.api.mappers.GooglePlacesDataMapper.toLocationDAO
import com.baymax.weather.forecast.fetch_location.api.mappers.GooglePlacesDataMapper.toPredictionsDAO
import com.baymax.weather.forecast.fetch_location.presentation.model.LocationDAO
import com.baymax.weather.forecast.utils.PrefHelper
import com.baymax.weather.forecast.utils.PrefHelper.keys.IS_LAST_LOCATION_CACHED
import com.baymax.weather.forecast.utils.PrefHelper.keys.LAT
import com.baymax.weather.forecast.utils.PrefHelper.keys.LNG
import com.baymax.weather.forecast.utils.get
import com.baymax.weather.forecast.utils.map
import com.baymax.weather.forecast.utils.set
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FetchLocationRepositoryImpl @Inject constructor(
    private val prefHelper: PrefHelper,
    private val fetchRemoteDataSource: FetchLocationRemoteDataSource,
) : FetchLocationRepository {

    override suspend fun getSuggestions(
        searchText: String,
    ) = withContext(Dispatchers.IO) {
        fetchRemoteDataSource.fetchPredictions(
            searchText,
            prefHelper.sharedPrefs[PrefHelper.GOOGLE_PLACE_API_KEY, ""],
        )
    }.map { toPredictionsDAO(it) }

    override suspend fun getCoordinates(
        placeId: String,
    ) = withContext(Dispatchers.IO) {
        fetchRemoteDataSource.fetchCoordinates(
            placeId,
            prefHelper.sharedPrefs[PrefHelper.GOOGLE_PLACE_API_KEY, ""],
        )
    }.map { toLocationDAO(it) }

    override suspend fun setLastLocation(location: LocationDAO) = withContext(Dispatchers.IO) {
        with(prefHelper) {
            val lat = location.lat.toString()
            val lng = location.lng.toString()
            if (sharedPrefs[LAT, "0.0"] != lat && sharedPrefs[LNG, "0.0"] != lng) {
                sharedPrefs[LAT] = lat
                sharedPrefs[LNG] = lng
                sharedPrefs[IS_LAST_LOCATION_CACHED] = true
            }
        }
    }

    override suspend fun getLastLocation(): LocationDAO = withContext(Dispatchers.IO) {
        val lat = prefHelper.sharedPrefs[LAT, "0.0"].toDoubleOrNull() ?: 0.0
        val lng = prefHelper.sharedPrefs[LNG, "0.0"].toDoubleOrNull() ?: 0.0
        LocationDAO(lat, lng)
    }

    override suspend fun isLocationCached(): Boolean = withContext(Dispatchers.IO) {
        prefHelper.sharedPrefs[IS_LAST_LOCATION_CACHED, false]
    }
}
