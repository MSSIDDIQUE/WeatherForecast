    package com.baymax.weatherforcast.api

import android.content.Context
import com.baymax.weatherforcast.api.response.WeatherResponse
import com.baymax.weatherforcast.utils.PrefHelper
import com.baymax.weatherforcast.utils.get
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

//http://api.openweathermap.org/data/2.5/forecast?q=Hyderabad,Telangana&appid=5f3f95dcfb462c164ddfce910fffe503
interface WeatherApiService {
    @GET("forecast")
    fun getWeather(
        @Query("q")
        location:String
    ):Deferred<WeatherResponse>

    @GET("forecast")
    suspend fun getWeatherOfCity(
        @Query("q")
        location: String
    ):Response<WeatherResponse>

    companion object{
        operator fun invoke(
            prefHelper:PrefHelper
        ): WeatherApiService {
            val requestInterceptor = Interceptor{ chain ->
                val url = chain.request()
                    .url
                    .newBuilder()
                    .addQueryParameter(
                        "appid",
                        prefHelper.sharedPref[PrefHelper.API_KEY,""]
                    )
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()
                return@Interceptor chain.proceed(request)
            }
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .build()
            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherApiService::class.java)
        }
    }
}