package com.baymax.weather.forecast.di

import android.app.Application
import android.content.Context
import com.baymax.weather.forecast.utils.PrefHelper
import dagger.Module
import dagger.Provides
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
    fun providePrefHelper(context: Context): PrefHelper {
        return PrefHelper(context)
    }
}
