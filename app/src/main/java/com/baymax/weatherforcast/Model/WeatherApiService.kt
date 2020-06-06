package com.baymax.weatherforcast.Model

import com.baymax.weatherforcast.Model.Network.ConnectivityInterceptor
import com.baymax.weatherforcast.Model.Response.WeatherResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val api_key = "5f3f95dcfb462c164ddfce910fffe503"
//http://api.openweathermap.org/data/2.5/forecast?q=Hyderabad,Telangana&appid=5f3f95dcfb462c164ddfce910fffe503
interface WeatherApiService {
    @GET("forecast")
    fun getWeather(
        @Query("q")
        location:String
    ):Deferred<WeatherResponse>

    companion object{
        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor
        ):WeatherApiService{
            var requestInterceptor = Interceptor{chain ->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("appid", api_key)
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()
                return@Interceptor chain.proceed(request)
            }
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(connectivityInterceptor)
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