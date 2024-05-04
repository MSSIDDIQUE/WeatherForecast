package com.baymax.weather.forecast.fetch_location.data

import com.baymax.weather.forecast.fetch_location.presentation.model.CoordinatesDAO
import javax.inject.Inject

class CacheLocationUseCase @Inject constructor(private val repo: FetchLocationRepository) {

    suspend fun cacheInSharedPrefs(location: CoordinatesDAO) = repo.setLastLocation(location)

    suspend fun isLastLocationCached() = repo.isLocationCached()
}
