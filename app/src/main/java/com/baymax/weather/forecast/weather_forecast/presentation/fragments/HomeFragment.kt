package com.baymax.weather.forecast.weather_forecast.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.baymax.weather.forecast.databinding.FragmentHomeBinding
import com.baymax.weather.forecast.presentation.fragments.BaseBindingFragment
import com.baymax.weather.forecast.presentation.view_state.BaseViewState
import com.baymax.weather.forecast.presentation.view_state.ProgressBarViewState
import com.baymax.weather.forecast.presentation.view_state.SnackBarViewState
import com.baymax.weather.forecast.weather_forecast.presentation.listeners.HomeFragmentEventListener
import com.baymax.weather.forecast.weather_forecast.presentation.model.WeatherReportsDAO
import com.baymax.weather.forecast.weather_forecast.presentation.screens.HomeScreen
import com.baymax.weather.forecast.weather_forecast.presentation.view_model.HomeFragmentViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : BaseBindingFragment<FragmentHomeBinding, HomeFragmentViewModel>(
    FragmentHomeBinding::inflate,
) {
    private var viewModel: HomeFragmentViewModel? = null

    private var eventListener: HomeFragmentEventListener? = null

    private var homeScreenState = mutableStateOf(WeatherReportsDAO())

    override fun onAttach(context: Context) {
        super.onAttach(context)
        eventListener = context as HomeFragmentEventListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            viewModel = getViewModelInstanceWithOwner(it)
        }
        binding.layoutWeatherReports.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                HomeScreen(homeScreenState)
            }
        }
        setupObservers()
    }

    private fun setupObservers() {
        observeWeather()
    }

    private fun observeWeather() = lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel?.weatherState?.collectLatest { uiState ->
                when (uiState) {
                    is BaseViewState.Success -> uiState.data?.let { data ->
                        setupWeatherSuccessView(data)
                    } ?: setupWeatherEmptyView()

                    is BaseViewState.Error -> viewModel?.run {
                        setProgressBarState(ProgressBarViewState.Hide)
                        setSnackBarState(SnackBarViewState.Error(uiState.errorMessage))
                    }

                    is BaseViewState.Loading -> setupWeatherLoadingView(uiState.msg)
                    is BaseViewState.Empty -> setupWeatherEmptyView()
                }
            }
        }
    }

    private fun setupWeatherSuccessView(weatherReports: WeatherReportsDAO) = with(binding) {
        layoutWeatherReports.visibility = View.VISIBLE
        homeScreenState.value = weatherReports
        viewModel?.setProgressBarState(ProgressBarViewState.Hide)
    }

    private fun setupWeatherEmptyView() = with(binding) {
        viewModel?.setProgressBarState(ProgressBarViewState.Hide)
        layoutWeatherReports.visibility = View.GONE
    }

    private fun setupWeatherLoadingView(msg: String) = with(binding) {
        viewModel?.setProgressBarState(ProgressBarViewState.Show(msg))
        layoutWeatherReports.visibility = View.GONE
    }
}
