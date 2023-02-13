package com.baymax.weather.forecast.presentation.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class ViewModelFactory @Inject constructor(
    private val viewModelProviderMap: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModelProvider = viewModelProviderMap[modelClass]
            ?: throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        return viewModelProvider.get() as T
    }
}
