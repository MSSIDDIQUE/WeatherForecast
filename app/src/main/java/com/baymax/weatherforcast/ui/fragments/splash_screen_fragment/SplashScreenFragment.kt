package com.baymax.weatherforcast.ui.fragments.splash_screen_fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.baymax.weatherforcast.R
import com.baymax.weatherforcast.databinding.FragmentSplashScreenBinding
import com.baymax.weatherforcast.ui.activities.MainActivity
import com.baymax.weatherforcast.ui.fragments.home_fragment.ui.HomeFragmentViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class SplashScreenFragment : DaggerFragment() {
    private var _binding: FragmentSplashScreenBinding? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: HomeFragmentViewModel
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
        viewModel = ViewModelProvider(
            requireActivity(),
            viewModelFactory
        ).get(HomeFragmentViewModel::class.java)
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
    }

    override fun onStart() {
        super.onStart()
        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.readyToFetch.observe(
                requireActivity(),
                { requirements ->
                    requirements?.let {
                        if (it["gps"] == true && it["network"] == true && it["permission"] == true) {
                            navController.navigate(R.id.homeFragment)
                        } else if (it["permission"] == false) {
                            (requireActivity() as MainActivity).requestLocationPermission()
                        } else if (it["network"] == false) {
                            navController.navigate(
                                R.id.errorFragment,
                                bundleOf("error_msg" to "No Internet Connection!")
                            )
                            Snackbar.make(
                                (requireActivity() as MainActivity).main_layout,
                                "No Internet Connection!",
                                Snackbar.LENGTH_LONG
                            ).show()
                        } else if (it["gps"] == false) {
                            (requireActivity() as MainActivity).turnOnGPS()
                            Snackbar.make(
                                (requireActivity() as MainActivity).main_layout,
                                "Turn on your GPS",
                                Snackbar.LENGTH_LONG
                            ).setAction(
                                "Retry"
                            ) {
                                (requireActivity() as MainActivity).turnOnGPS()
                            }.show()
                        }
                    }
                }
            )
        }, SPLASH_TIME_OUT)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}