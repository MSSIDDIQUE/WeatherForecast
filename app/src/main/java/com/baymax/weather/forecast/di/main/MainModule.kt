package com.baymax.weather.forecast.di.main

import android.content.Context
import com.baymax.weather.forecast.search_location.api.GooglePlaceApiService
import com.baymax.weather.forecast.search_location.data.SearchLocationRemoteDataSource
import com.baymax.weather.forecast.search_location.data.SearchLocationRepository
import com.baymax.weather.forecast.search_location.data.SearchLocationRepositoryImpl
import com.baymax.weather.forecast.weather_forecast.api.WeatherApiService
import com.baymax.weather.forecast.weather_forecast.data.WeatherRemoteDataSource
import com.baymax.weather.forecast.weather_forecast.data.WeatherRepository
import com.baymax.weather.forecast.weather_forecast.data.WeatherRepositoryImpl
import com.baymax.weather.forecast.utils.ConnectionLiveData
import com.baymax.weather.forecast.utils.Constants
import com.baymax.weather.forecast.utils.PrefHelper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class MainModule {

    @MainScope
    @Provides
    fun provideWeatherApiServices(
        client: OkHttpClient,
        converterFactory: GsonConverterFactory
    ): WeatherApiService {
        return getDynamicRetrofitClient(
            client,
            converterFactory,
            Constants.WEATHER_API_BASE_URL
        ).create(WeatherApiService::class.java)
    }

    @MainScope
    @Provides
    fun provideGooglePlaceApiServices(
        client: OkHttpClient,
        converterFactory: GsonConverterFactory
    ): GooglePlaceApiService {
        return getDynamicRetrofitClient(
            client,
            converterFactory,
            Constants.GOOGLE_PLACE_API_BASE_URL
        ).create(GooglePlaceApiService::class.java)
    }

    @MainScope
    @Provides
    fun provideWeatherRemoteDataSource(
        weatherApiService: WeatherApiService,
        prefHelper: PrefHelper
    ): WeatherRemoteDataSource {
        return WeatherRemoteDataSource(
            weatherApiService,
            prefHelper
        )
    }

    @MainScope
    @Provides
    fun provideSearchLocationRemoteDataSource(
        googlePlaceApiService: GooglePlaceApiService,
        prefHelper: PrefHelper
    ): SearchLocationRemoteDataSource = SearchLocationRemoteDataSource(
        googlePlaceApiService,
        prefHelper
    )

    @MainScope
    @Provides
    fun provideLocationProviderClient(
        context: Context
    ): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

    @MainScope
    @Provides
    fun provideWeatherRepository(
        remoteDataSource: WeatherRemoteDataSource
    ): WeatherRepository = WeatherRepositoryImpl(remoteDataSource)

    private fun getDynamicRetrofitClient(
        client: OkHttpClient,
        converterFactory: GsonConverterFactory,
        baseUrl: String
    ): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(baseUrl)
            .addConverterFactory(converterFactory)
            .build()
    }

    @MainScope
    @Provides
    fun provideSearchLocationRepository(
        remoteDataSource: SearchLocationRemoteDataSource
    ): SearchLocationRepository = SearchLocationRepositoryImpl(remoteDataSource)

    @MainScope
    @Provides
    fun provideConnectionLiveData(
        context: Context
    ): ConnectionLiveData {
        return ConnectionLiveData(context)
    }
}
