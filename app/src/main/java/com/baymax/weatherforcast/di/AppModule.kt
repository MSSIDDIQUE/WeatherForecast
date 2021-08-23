package com.baymax.weatherforcast.di

import android.app.Application
import android.content.Context
import com.baymax.weatherforcast.BuildConfig
import com.baymax.weatherforcast.utils.Constants
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
abstract class AppModule {

    @Singleton
    @Provides
    private fun provideContext(application: Application): Context {
        return application
    }

    @Singleton
    @Provides
    private fun provideConvertorFactory(): GsonConverterFactory {
        return GsonConverterFactory.create(Gson())
    }

    @Singleton
    @Provides
    private fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    @Singleton
    @Provides
    fun provideOkhttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        client: OkHttpClient,
        converterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(converterFactory)
            .build()
    }

    @Singleton
    @Provides
    private fun <T> provideWeatherApiServices(
        retrofit: Retrofit,
        clazz: Class<T>
    ): T {
        return retrofit.create(clazz)
    }

}