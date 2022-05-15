package com.baymax.weatherforecast.di

import androidx.lifecycle.ViewModelProvider
import com.baymax.weatherforecast.ui.view_model.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}