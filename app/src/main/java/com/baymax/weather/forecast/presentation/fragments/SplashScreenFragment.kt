package com.baymax.weather.forecast.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.baymax.weather.forecast.R
import com.baymax.weather.forecast.weather_forecast.presentation.screens.SplashScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenFragment : Fragment() {

    private lateinit var composeView: ComposeView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).also { composeView = it }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        composeView.setContent { SplashScreen() }
        navigateToHomeScreen()
    }

    private fun navigateToHomeScreen() = lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            delay(4000)
            findNavController().navigate(R.id.homeFragment)
        }
    }
}
