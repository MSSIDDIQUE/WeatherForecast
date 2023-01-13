package com.baymax.weather.forecast.presentation.fragments

import android.os.Handler
import android.os.Looper
import androidx.navigation.fragment.findNavController
import com.baymax.weather.forecast.R
import com.baymax.weather.forecast.databinding.FragmentSplashScreenBinding
import com.baymax.weather.forecast.usecases.weather_forecast.presentation.view_model.HomeFragmentViewModel

class SplashScreenFragment : BaseBindingFragment<FragmentSplashScreenBinding, HomeFragmentViewModel>(
    FragmentSplashScreenBinding::inflate
) {
    override fun onStart() {
        super.onStart()
        Handler(Looper.getMainLooper()).postDelayed({
            findNavController().navigate(R.id.homeFragment)
        }, 4000)
    }
}
