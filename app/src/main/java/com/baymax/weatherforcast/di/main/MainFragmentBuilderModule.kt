package com.baymax.weatherforcast.di.main

import com.baymax.weatherforcast.ui.fragments.error_fragment.ErrorFragment
import com.baymax.weatherforcast.ui.fragments.home_fragment.ui.HomeFragment
import com.baymax.weatherforcast.ui.fragments.splash_screen_fragment.SplashScreenFragment
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