package com.baymax.weatherforcast.di.main

import androidx.lifecycle.ViewModel
import com.baymax.weatherforcast.di.ViewModelKey
import com.baymax.weatherforcast.ui.fragments.home_fragment.ui.HomeFragmentViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(HomeFragmentViewModel::class)
    abstract fun bindHomeFragmentViewModel(viewModel: HomeFragmentViewModel): ViewModel
}