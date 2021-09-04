package com.baymax.weatherforcast.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.baymax.weatherforcast.ui.ViewModelFactory
import com.baymax.weatherforcast.ui.fragments.home_fragment.ui.HomeFragmentViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelFactoryModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}