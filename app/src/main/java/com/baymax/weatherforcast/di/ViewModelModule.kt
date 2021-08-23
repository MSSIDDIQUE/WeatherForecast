package com.baymax.weatherforcast.di

import androidx.lifecycle.ViewModel
import com.baymax.weatherforcast.ui.fragments.home_fragment.ui.HomeFramentViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(HomeFramentViewModel::class)
    abstract fun bindHomeFragmentViewModel(veiwModel: HomeFramentViewModel): ViewModel
}