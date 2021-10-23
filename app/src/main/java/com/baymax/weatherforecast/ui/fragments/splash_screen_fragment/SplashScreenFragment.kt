package com.baymax.weatherforecast.ui.fragments.splash_screen_fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.baymax.weatherforecast.R
import com.baymax.weatherforecast.databinding.FragmentSplashScreenBinding

class SplashScreenFragment : Fragment() {
    private var _binding: FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    companion object {
        private const val SPLASH_TIME_OUT: Long = 4000
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
    }

    override fun onStart() {
        super.onStart()
        Handler(Looper.getMainLooper()).postDelayed({
            navController.navigate(R.id.homeFragment)
        }, SPLASH_TIME_OUT)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}