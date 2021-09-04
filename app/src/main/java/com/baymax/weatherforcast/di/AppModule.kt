package com.baymax.weatherforcast.di

import android.app.Application
import android.content.Context
import com.baymax.weatherforcast.BuildConfig
import com.baymax.weatherforcast.utils.Constants
import com.baymax.weatherforcast.utils.PrefHelper
import com.baymax.weatherforcast.utils.get
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideContext(application: Application): Context {
        return application
    }

    @Singleton
    @Provides
    fun provideConvertorFactory(): GsonConverterFactory {
        return GsonConverterFactory.create(Gson())
    }

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
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
    fun providePrefHelper(context: Context): PrefHelper {
        return PrefHelper(context)
    }

    @Singleton
    @Provides
    fun provideRequestInterceptor(
        prefHelper: PrefHelper
    ): Interceptor {
        return Interceptor { chain ->
            val url = chain.request()
                .url
                .newBuilder()
                .addQueryParameter(
                    PrefHelper.API_KEY,
                    prefHelper.sharedPref[PrefHelper.API_KEY, ""]
                )
                .build()
            val request = chain.request()
                .newBuilder()
                .url(url)
                .build()
            return@Interceptor chain.proceed(request)
        }
    }

    @Singleton
    @Provides
    fun provideOkhttpClient(
        requestInterceptor: Interceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .addInterceptor(loggingInterceptor)
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
}