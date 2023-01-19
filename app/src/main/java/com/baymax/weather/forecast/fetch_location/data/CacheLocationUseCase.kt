package com.baymax.weather.forecast.fetch_location.data

import com.baymax.weather.forecast.fetch_location.api.data_transfer_model.Location
import javax.inject.Inject

class CacheLocationUseCase @Inject constructor(private val repo: FetchLocationRepository) {

    suspend fun cacheInSharedPrefs(location: Location) = repo.setLastLocation(location)

    suspend fun isLastLocationCached() = repo.isLocationCached()
}
