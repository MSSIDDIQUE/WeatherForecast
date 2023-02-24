package com.baymax.weather.forecast.di.main

import androidx.lifecycle.ViewModel
import com.baymax.weather.forecast.di.ViewModelKey
import com.baymax.weather.forecast.weather_forecast.presentation.view_model.HomeScreenViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(HomeScreenViewModel::class)
    abstract fun bindHomeFragmentViewModel(viewModel: HomeScreenViewModel): ViewModel
}
