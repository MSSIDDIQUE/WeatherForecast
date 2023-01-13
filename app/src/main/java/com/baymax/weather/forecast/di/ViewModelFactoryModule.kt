package com.baymax.weather.forecast.di

import androidx.lifecycle.ViewModelProvider
import com.baymax.weather.forecast.presentation.view_models.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
