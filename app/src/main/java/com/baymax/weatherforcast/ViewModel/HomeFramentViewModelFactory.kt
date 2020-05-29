package com.baymax.weatherforcast.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.baymax.weatherforcast.Model.Repository.Repository
import com.baymax.weatherforcast.Model.Repository.RepositoryImpl

class HomeFramentViewModelFactory(
    private val repo: Repository
): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel?> create(modelClass:Class<T>):T{
        return HomeFramentViewModel(repo)   as T
    }
}