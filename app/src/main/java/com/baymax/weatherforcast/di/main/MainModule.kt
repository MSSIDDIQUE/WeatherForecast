package com.baymax.weatherforcast.di.main

import android.content.Context
import com.baymax.weatherforcast.api.WeatherApiService
import com.baymax.weatherforcast.ui.fragments.home_fragment.data.LocationProvider
import com.baymax.weatherforcast.ui.fragments.home_fragment.data.WeatherRemoteDataSource
import com.baymax.weatherforcast.ui.fragments.home_fragment.data.WeatherRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class MainModule {

    @MainScope
    @Provides
    fun provideWeatherApiServices(
        retrofit: Retrofit
    ): WeatherApiService {
        return retrofit.create(WeatherApiService::class.java)
    }

    @MainScope
    @Provides
    fun provideRemoteDataSource(
        apiService: WeatherApiService
    ): WeatherRemoteDataSource {
        return WeatherRemoteDataSource(apiService)
    }

    @MainScope
    @Provides
    fun provideLocationProviderClient(
        context: Context
    ): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

    @MainScope
    @Provides
    fun provideLocationProvider(
        context: Context,
        client: FusedLocationProviderClient
    ): LocationProvider {
        return LocationProvider(context, client)
    }

    @MainScope
    @Provides
    fun provideRepository(
        remoteDataSource: WeatherRemoteDataSource,
        locationProvider: LocationProvider
    ): WeatherRepository {
        return WeatherRepository(
            remoteDataSource,
            locationProvider
        )
    }
}