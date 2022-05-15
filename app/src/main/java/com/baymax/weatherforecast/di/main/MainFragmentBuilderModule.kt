package com.baymax.weatherforecast.di.main

import com.baymax.weatherforecast.ui.fragments.HomeFragment
import com.baymax.weatherforecast.ui.bottom_sheets.WeatherDetailsBottomSheet
import com.baymax.weatherforecast.ui.fragments.SplashScreenFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentBuilderModule {
    @ContributesAndroidInjector
    abstract fun contributeSplashScreenFragment(): SplashScreenFragment

    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributeWeatherDetailsBottomSheet(): WeatherDetailsBottomSheet
}