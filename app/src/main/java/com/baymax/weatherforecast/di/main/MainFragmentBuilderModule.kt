package com.baymax.weatherforecast.di.main

import com.baymax.weatherforecast.ui.fragments.home_fragment.ui.HomeFragment
import com.baymax.weatherforecast.ui.fragments.home_fragment.ui.WeatherDetailsBottomSheet
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentBuilderModule {
    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributeWeatherDetailsBottomSheet(): WeatherDetailsBottomSheet
}