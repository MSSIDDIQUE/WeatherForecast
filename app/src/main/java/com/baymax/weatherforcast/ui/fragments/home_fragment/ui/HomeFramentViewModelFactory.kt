package com.baymax.weatherforcast.ui.fragments.home_fragment.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.baymax.weatherforcast.ui.fragments.home_fragment.data.WeatherRepository

class HomeFramentViewModelFactory(
    private val repo: WeatherRepository
): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel?> create(modelClass:Class<T>):T{
        return HomeFramentViewModel(repo)   as T
    }
}