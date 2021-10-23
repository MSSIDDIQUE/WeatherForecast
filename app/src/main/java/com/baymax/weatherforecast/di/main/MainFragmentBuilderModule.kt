package com.baymax.weatherforecast.di.main

import com.baymax.weatherforecast.ui.fragments.error_fragment.ErrorFragment
import com.baymax.weatherforecast.ui.fragments.home_fragment.ui.HomeFragment
import com.baymax.weatherforecast.ui.fragments.splash_screen_fragment.SplashScreenFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentBuilderModule {
    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributeErrorFragment(): ErrorFragment

    @ContributesAndroidInjector
    abstract fun contributeSplashScreenFragment(): SplashScreenFragment
}