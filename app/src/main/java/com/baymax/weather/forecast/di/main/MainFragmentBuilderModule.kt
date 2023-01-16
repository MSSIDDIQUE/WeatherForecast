package com.baymax.weather.forecast.di.main

import com.baymax.weather.forecast.presentation.fragments.SplashScreenFragment
import com.baymax.weather.forecast.weather_forecast.presentation.fragments.HomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentBuilderModule {
    @ContributesAndroidInjector
    abstract fun contributeSplashScreenFragment(): SplashScreenFragment

    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment
}
