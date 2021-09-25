package com.baymax.weatherforcast.di

import androidx.lifecycle.ViewModelProvider
import com.baymax.weatherforcast.ui.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}