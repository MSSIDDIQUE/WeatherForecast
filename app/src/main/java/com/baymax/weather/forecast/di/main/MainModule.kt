package com.baymax.weather.forecast.di.main

import android.content.Context
import com.baymax.weather.forecast.data.HttpExceptions
import com.baymax.weather.forecast.di.MainScope
import com.baymax.weather.forecast.fetch_location.api.GooglePlaceApiService
import com.baymax.weather.forecast.fetch_location.api.GooglePlaceApiServiceImpl
import com.baymax.weather.forecast.fetch_location.data.FetchLocationRemoteDataSource
import com.baymax.weather.forecast.fetch_location.data.FetchLocationRepository
import com.baymax.weather.forecast.fetch_location.data.FetchLocationRepositoryImpl
import com.baymax.weather.forecast.utils.Constants.Companion.GOOGLE_PLACE_API_BASE_URL
import com.baymax.weather.forecast.utils.Constants.Companion.WEATHER_API_BASE_URL
import com.baymax.weather.forecast.utils.PrefHelper
import com.baymax.weather.forecast.utils.get
import com.baymax.weather.forecast.weather_forecast.api.WeatherApiService
import com.baymax.weather.forecast.weather_forecast.api.WeatherApiServiceImpl
import com.baymax.weather.forecast.weather_forecast.data.WeatherRemoteDataSource
import com.baymax.weather.forecast.weather_forecast.data.WeatherRepository
import com.baymax.weather.forecast.weather_forecast.data.WeatherRepositoryImpl
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import io.ktor.serialization.gson.gson
import okhttp3.logging.HttpLoggingInterceptor

@Module
class MainModule {

    @MainScope
    @Provides
    fun provideWeatherApiServices(
        prefHelper: PrefHelper
    ): WeatherApiService {
        return WeatherApiServiceImpl(
            createDynamicHttpClient(
                baseUrl = WEATHER_API_BASE_URL,
                apiKey = Pair(
                    PrefHelper.WEATHER_API_KEY,
                    prefHelper.sharedPrefs[PrefHelper.WEATHER_API_KEY, ""]
                )
            )
        )
    }

    @MainScope
    @Provides
    fun provideGooglePlaceApiServices(
        prefHelper: PrefHelper
    ): GooglePlaceApiService {
        return GooglePlaceApiServiceImpl(
            createDynamicHttpClient(
                baseUrl = GOOGLE_PLACE_API_BASE_URL,
                apiKey = Pair(
                    PrefHelper.GOOGLE_PLACE_API_KEY,
                    prefHelper.sharedPrefs[PrefHelper.GOOGLE_PLACE_API_KEY, ""]
                )
            )
        )
    }

    @MainScope
    @Provides
    fun provideWeatherRemoteDataSource(
        weatherApiService: WeatherApiService
    ): WeatherRemoteDataSource {
        return WeatherRemoteDataSource(
            weatherApiService
        )
    }

    @MainScope
    @Provides
    fun provideSearchLocationRemoteDataSource(
        googlePlaceApiService: GooglePlaceApiService,
    ): FetchLocationRemoteDataSource = FetchLocationRemoteDataSource(googlePlaceApiService)

    @MainScope
    @Provides
    fun provideLocationProviderClient(
        context: Context,
    ): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

    @MainScope
    @Provides
    fun provideWeatherRepository(
        remoteDataSource: WeatherRemoteDataSource,
    ): WeatherRepository = WeatherRepositoryImpl(remoteDataSource)

    @MainScope
    @Provides
    fun provideSearchLocationRepository(
        prefHelper: PrefHelper,
        remoteDataSource: FetchLocationRemoteDataSource,
    ): FetchLocationRepository = FetchLocationRepositoryImpl(prefHelper, remoteDataSource)

    private fun createDynamicHttpClient(
        baseUrl: String,
        apiKey: Pair<String, String>? = null
    ): HttpClient {
        return HttpClient(OkHttp) {
            expectSuccess = true
            engine {
                addInterceptor(HttpLoggingInterceptor().apply {
                    setLevel(HttpLoggingInterceptor.Level.BODY)
                })
            }
            defaultRequest {
                url(baseUrl)
                apiKey?.let{ url.parameters.append(it.first, it.second) }
            }
            install(ContentNegotiation) { gson() }
            HttpResponseValidator {
                validateResponse { response ->
                    if (!response.status.isSuccess()) {
                        val httpFailureReason = when (response.status) {
                            HttpStatusCode.Unauthorized -> "Unauthorized request"
                            HttpStatusCode.Forbidden -> "${response.status.value} Missing API key"
                            HttpStatusCode.NotFound -> "Invalid Request"
                            HttpStatusCode.UpgradeRequired -> "Upgrade to VIP"
                            HttpStatusCode.RequestTimeout -> "Network Timeout"
                            in HttpStatusCode.InternalServerError..HttpStatusCode.GatewayTimeout -> "${response.status.value} Server Error"
                            else -> "Network error!"
                        }

                        throw HttpExceptions(
                            response = response,
                            cachedResponseText = response.bodyAsText(),
                            failureReason = httpFailureReason,
                        )
                    }
                }
            }
        }
    }
}
