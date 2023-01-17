package com.baymax.weather.forecast.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.baymax.weather.forecast.R
import com.baymax.weather.forecast.databinding.FragmentSplashScreenBinding
import com.baymax.weather.forecast.weather_forecast.presentation.view_model.HomeFragmentViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenFragment :
    BaseBindingFragment<FragmentSplashScreenBinding, HomeFragmentViewModel>(
        FragmentSplashScreenBinding::inflate
    ) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigateToHomeScreen()
    }

    private fun navigateToHomeScreen() = lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            delay(4000)
            findNavController().navigate(R.id.homeFragment)
        }
    }
}
