package com.baymax.weatherforecast.di.main

import android.content.Context
import com.baymax.weatherforecast.api.GooglePlaceApiService
import com.baymax.weatherforecast.api.WeatherApiService
import com.baymax.weatherforecast.ui.fragments.home_fragment.data.WeatherRemoteDataSource
import com.baymax.weatherforecast.ui.fragments.home_fragment.data.WeatherRepository
import com.baymax.weatherforecast.utils.ConnectionLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.baymax.weatherforecast.utils.Constants
import com.baymax.weatherforecast.utils.PrefHelper
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
        converterFactory: GsonConverterFactory,
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
        converterFactory: GsonConverterFactory,
    ): GooglePlaceApiService {
        return getDynamicRetrofitClient(
            client,
            converterFactory,
            Constants.GOOGLE_PLACE_API_BASE_URL
        ).create(GooglePlaceApiService::class.java)
    }

    @MainScope
    @Provides
    fun provideRemoteDataSource(
        weatherApiService: WeatherApiService,
        googlePlaceApiService: GooglePlaceApiService,
        prefHelper: PrefHelper
    ): WeatherRemoteDataSource {
        return WeatherRemoteDataSource(
            weatherApiService,
            googlePlaceApiService,
            prefHelper
        )
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
    fun provideRepository(
        remoteDataSource: WeatherRemoteDataSource
    ): WeatherRepository {
        return WeatherRepository(
            remoteDataSource
        )
    }

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
    fun provideConnectionLiveData(
        context: Context
    ): ConnectionLiveData {
        return ConnectionLiveData(context)
    }
}