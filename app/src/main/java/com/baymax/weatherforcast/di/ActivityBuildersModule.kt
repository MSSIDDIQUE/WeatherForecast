package com.baymax.weatherforcast.di

import com.baymax.weatherforcast.ui.activities.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeMainActivity():MainActivity
}