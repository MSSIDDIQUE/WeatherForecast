package com.baymax.weatherforcast.di

import com.baymax.weatherforcast.di.main.MainFragmentBuilderModule
import com.baymax.weatherforcast.di.main.MainModule
import com.baymax.weatherforcast.di.main.MainScope
import com.baymax.weatherforcast.di.main.MainViewModelModule
import com.baymax.weatherforcast.ui.activities.MainActivity
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