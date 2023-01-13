package com.baymax.weather.forecast.di

import com.baymax.weather.forecast.di.main.MainFragmentBuilderModule
import com.baymax.weather.forecast.di.main.MainModule
import com.baymax.weather.forecast.di.main.MainScope
import com.baymax.weather.forecast.di.main.MainViewModelModule
import com.baymax.weather.forecast.usecases.weather_forecast.presentation.activities.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {
    @MainScope
    @ContributesAndroidInjector(
        modules = [
            MainFragmentBuilderModule::class,
            MainModule::class,
            MainViewModelModule::class
        ]
    )
    abstract fun contributeMainActivity(): MainActivity
}
