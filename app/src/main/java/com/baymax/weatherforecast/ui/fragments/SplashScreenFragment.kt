package com.baymax.weatherforecast.ui.fragments

import android.os.Handler
import android.os.Looper
import androidx.navigation.fragment.findNavController
import com.baymax.weatherforecast.R
import com.baymax.weatherforecast.databinding.FragmentSplashScreenBinding
import com.baymax.weatherforecast.ui.view_model.HomeFragmentViewModel

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