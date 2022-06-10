package com.baymax.weatherforecast.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.baymax.weatherforecast.R
import com.baymax.weatherforecast.api.google_place_api.Prediction
import com.baymax.weatherforecast.api.weather_api.domain_model.ApiResponseDM
import com.baymax.weatherforecast.data.Result
import com.baymax.weatherforecast.data.UiState
import com.baymax.weatherforecast.databinding.FragmentHomeBinding
import com.baymax.weatherforecast.ui.activities.MainActivity
import com.baymax.weatherforecast.ui.view_model.HomeFragmentViewModel
import com.baymax.weatherforecast.ui.adapters.WeatherListAdapter
import com.baymax.weatherforecast.utils.DateTimeUtils.getCurrentDataTime
import com.baymax.weatherforecast.utils.isGPSActive
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HomeFragment : BaseBindingFragment<FragmentHomeBinding, HomeFragmentViewModel>(
    FragmentHomeBinding::inflate
), WeatherListAdapter.WeatherDetailsItemListener {

    private lateinit var linearLayoutManager: LinearLayoutManager

    companion object {
        private const val MULTIPLE_LOCATION_PERMISSION = 1
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            homeFragmentViewModel = viewModel
        }
        setupClickListeners()
    }

    override fun onResume() {
        super.onResume()
        setupObservers()
    }

    private fun setupClickListeners() = binding.apply {
        includedView.apply {
            viewMoreOrLessContainer.setOnClickListener {
                if (!hiddenView.isVisible) {
                    TransitionManager.beginDelayedTransition(
                        cardTemp,
                        AutoTransition()
                    )
                    hiddenView.visibility = View.VISIBLE
                    viewMoreOrLess.text = getString(R.string.view_less)
                } else {
                    hiddenView.visibility = View.GONE
                    viewMoreOrLess.text = getString(R.string.view_more)
                    TransitionManager.beginDelayedTransition(
                        cardTemp,
                        AutoTransition()
                    )
                }
            }
        }
        btnUseMyLocation.setOnClickListener {
            getLocationAndStartObservingWeather()
        }
        searchText.setOnItemClickListener { adapterView, _, position, _ ->
            val selectedLocation = adapterView.getItemAtPosition(position).toString()
            viewModel.run {
                placeIdMap[selectedLocation]?.let {
                    viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                        val result = withContext(Dispatchers.IO) {
                            getCoordinates(it)
                        }
                        when (result) {
                            is Result.Failure -> result.msg?.let { msg ->
                                showSnackBar(msg)
                            }
                            is Result.Success -> {
                                val location = result.data.result.geometry.location
                                viewModel.setUiState(UiState.Loading("Fetching Weather"))
                                getWeather(location)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setupObservers() {
        observeSuggestions()
        observeWeather()
        getLocationAndStartObservingWeather()
    }

    private fun observeSuggestions() {
        binding.searchText.doAfterTextChanged { text ->
            viewModel.setSearchQuery(text.toString())
        }
        viewModel.suggestionsLiveData.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is UiState.Empty -> TODO()
                is UiState.Error -> showSnackBar(uiState.errorMessage)
                is UiState.Loading -> TODO()
                is UiState.Success -> uiState.data?.predictions?.let { listOfPredictions ->
                    updateListOfPredictions(listOfPredictions)
                }
            }
        }
    }

    private fun updateListOfPredictions(predictions: List<Prediction>) = binding.apply {
        viewModel.placeIdMap.clear()
        predictions.forEach { prediction->
            viewModel.placeIdMap[prediction.description] = prediction.placeId
        }
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.support_simple_spinner_dropdown_item,
            predictions.map { it.description }
        )
        searchText.setAdapter(adapter)
    }

    private fun observeWeather() = binding.apply {
        viewModel.weatherData.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is UiState.Success -> uiState.data?.let { data ->
                    setupWeatherSuccessView(data)
                } ?: setupWeatherEmptyView()
                is UiState.Error -> showSnackBar(uiState.errorMessage)
                is UiState.Loading -> setupWeatherLoadingView(uiState.msg)
                is UiState.Empty -> setupWeatherEmptyView()
            }
        }
    }

    private fun setupWeatherSuccessView(data: ApiResponseDM) = binding.apply {
        cardTemp.visibility = View.VISIBLE
        rvContainer.visibility = View.VISIBLE
        lineWeatherForecastContainer.visibility = View.VISIBLE
        if (data.dataGroupedByDate.isEmpty()) {
            return@apply
        }
        linearLayoutManager = LinearLayoutManager(context)
        location = data.city
        data.dataGroupedByTime[getCurrentDataTime()["time"]]?.let { recentWeatherReport ->
            currentweather = recentWeatherReport[0]
            recyclerView.apply {
                adapter = WeatherListAdapter(
                    recentWeatherReport,
                    this@HomeFragment
                )
                layoutManager = linearLayoutManager
            }
        }
        showProgressBar(false)
    }

    private fun setupWeatherEmptyView() = binding.apply {
        showProgressBar(false)
        cardTemp.visibility = View.GONE
        rvContainer.visibility = View.GONE
        lineWeatherForecastContainer.visibility = View.GONE
    }

    private fun setupWeatherLoadingView(msg: String) = binding.apply {
        showProgressBar(true, msg)
        cardTemp.visibility = View.GONE
        rvContainer.visibility = View.GONE
        lineWeatherForecastContainer.visibility = View.GONE
    }

    override fun onItemClick(date: String) {
        val direction = HomeFragmentDirections
            .actionHomeFragmentToWeatherDetailsBottomSheet()
            .setDate(date.split(" ")[0])
        findNavController().navigate(direction)
    }

    fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            MULTIPLE_LOCATION_PERMISSION
        )
    }

    fun hasLocationPermission(): Boolean {
        arrayListOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ).forEach { permission ->
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    fun getLocationAndStartObservingWeather() {
        binding.searchText.setText("")
        when {
            hasLocationPermission() && requireContext().isGPSActive() -> viewModel.startCollectingDeviceLocationAndFetchWeather()
            !hasLocationPermission() -> requestLocationPermission()
            else -> (activity as MainActivity).turnOnGPS()
        }
    }
}
