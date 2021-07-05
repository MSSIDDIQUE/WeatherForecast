package com.baymax.weatherforcast.ui.fragments.splash_screen_fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.baymax.weatherforcast.R
import com.baymax.weatherforcast.databinding.FragmentSplashScreenBinding
import com.baymax.weatherforcast.ui.activities.MainActivity
import com.baymax.weatherforcast.ui.fragments.home_fragment.ui.HomeFramentViewModel
import com.baymax.weatherforcast.ui.fragments.home_fragment.ui.HomeFramentViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class SplashScreenFragment : Fragment(), KodeinAware {

    override val kodeinContext = kcontext<Fragment>(this)
    override val kodein by kodein()
    private var _binding: FragmentSplashScreenBinding? = null
    private val viewModelFactory: HomeFramentViewModelFactory by instance()
    private lateinit var viewModel: HomeFramentViewModel
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    companion object {
        private const val SPLASH_TIME_OUT: Long = 5000
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
        ).get(HomeFramentViewModel::class.java)
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
    }

    override fun onStart() {
        super.onStart()
        viewModel.readyToFetch.observe(
            requireActivity(),
            { requirements ->
                requirements?.let {
                    if (it["gps"] == true && it["network"] == true && it["permission"] == true) {
                        val direction =
                            SplashScreenFragmentDirections.actionSplashScreenFragmentToHomeFragment()
                        Handler(Looper.getMainLooper()).postDelayed({
                            Log.d("saquib","current destination is ${navController.currentDestination.toString()}")
                            navController.navigate(direction)
                        }, SPLASH_TIME_OUT)
                    } else if (it["permission"] == false) {
                        (requireActivity() as MainActivity).requestLocationPermission()
                    } else if (it["network"] == false) {
                        val direction =
                            SplashScreenFragmentDirections.actionSplashScreenFragmentToErrorFragment(
                                "No internet connection"
                            )
                        navController.navigate(direction)
                        Snackbar.make(
                            (requireActivity() as MainActivity).main_layout,
                            "No Internet Connection",
                            Snackbar.LENGTH_LONG
                        ).show()
                    } else if (it["gps"] == false) {
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
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}