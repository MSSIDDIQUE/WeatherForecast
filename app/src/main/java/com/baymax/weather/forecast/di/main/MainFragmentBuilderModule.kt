package com.baymax.weather.forecast.di.main

import com.baymax.weather.forecast.ui.fragments.HomeFragment
import com.baymax.weather.forecast.ui.fragments.SplashScreenFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentBuilderModule {
    @ContributesAndroidInjector
    abstract fun contributeSplashScreenFragment(): SplashScreenFragment

    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment
}