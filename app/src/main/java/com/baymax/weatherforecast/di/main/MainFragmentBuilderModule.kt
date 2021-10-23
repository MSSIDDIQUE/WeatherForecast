package com.baymax.weatherforecast.di.main

import com.baymax.weatherforecast.ui.fragments.home_fragment.ui.HomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentBuilderModule {
    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment
}