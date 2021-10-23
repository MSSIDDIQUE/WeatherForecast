package com.baymax.weatherforecast.di

import com.baymax.weatherforecast.di.main.MainFragmentBuilderModule
import com.baymax.weatherforecast.di.main.MainModule
import com.baymax.weatherforecast.di.main.MainScope
import com.baymax.weatherforecast.di.main.MainViewModelModule
import com.baymax.weatherforecast.ui.activities.MainActivity
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