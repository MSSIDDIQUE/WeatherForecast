package com.baymax.weather.forecast.fetch_location.data

import com.baymax.weather.forecast.fetch_location.presentation.model.CoordinatesDAO
import com.baymax.weather.forecast.utils.PrefHelper
import com.baymax.weather.forecast.utils.PrefHelper.keys.IS_LAST_LOCATION_CACHED
import com.baymax.weather.forecast.utils.PrefHelper.keys.LAT
import com.baymax.weather.forecast.utils.PrefHelper.keys.LNG
import com.baymax.weather.forecast.utils.get
import com.baymax.weather.forecast.utils.set
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FetchLocationRepositoryImpl @Inject constructor(
    private val prefHelper: PrefHelper,
    private val fetchRemoteDataSource: FetchLocationRemoteDataSource,
) : FetchLocationRepository {

    override suspend fun getSuggestions(searchText: String) = fetchRemoteDataSource.fetchPredictions(searchText)

    override suspend fun getCoordinates(placeId: String) = fetchRemoteDataSource.fetchCoordinates(placeId)

    override suspend fun setLastLocation(location: CoordinatesDAO) = withContext(Dispatchers.IO) {
        with(prefHelper) {
            val lat = location.lat.toString()
            val lng = location.lng.toString()
            if (sharedPrefs[LAT, "0.0"] != lat || sharedPrefs[LNG, "0.0"] != lng) {
                sharedPrefs[LAT] = lat
                sharedPrefs[LNG] = lng
                sharedPrefs[IS_LAST_LOCATION_CACHED] = true
            }
        }
    }

    override suspend fun getLastLocation(): CoordinatesDAO = withContext(Dispatchers.IO) {
        CoordinatesDAO(
            lat = prefHelper.sharedPrefs[LAT, "0.0"].toDoubleOrNull() ?: 0.0,
            lng = prefHelper.sharedPrefs[LNG, "0.0"].toDoubleOrNull() ?: 0.0
        )
    }

    override suspend fun isLocationCached(): Boolean = withContext(Dispatchers.IO) {
        prefHelper.sharedPrefs[IS_LAST_LOCATION_CACHED, false]
    }
}
