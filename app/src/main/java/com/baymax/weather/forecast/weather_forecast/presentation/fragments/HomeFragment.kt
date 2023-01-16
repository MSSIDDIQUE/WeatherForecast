package com.baymax.weather.forecast.weather_forecast.presentation.fragments

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.baymax.weather.forecast.R
import com.baymax.weather.forecast.data.BaseViewState
import com.baymax.weather.forecast.databinding.FragmentHomeBinding
import com.baymax.weather.forecast.databinding.ItemViewWeatherDetailsBinding
import com.baymax.weather.forecast.presentation.fragments.BaseBindingFragment
import com.baymax.weather.forecast.search_location.api.data_transfer_model.Location
import com.baymax.weather.forecast.utils.DateTimeUtils.getCurrentDateTime
import com.baymax.weather.forecast.utils.isGPSActive
import com.baymax.weather.forecast.weather_forecast.api.domain_model.ApiResponseDM
import com.baymax.weather.forecast.weather_forecast.api.domain_model.WeatherDM
import com.baymax.weather.forecast.weather_forecast.presentation.activities.MainActivity
import com.baymax.weather.forecast.weather_forecast.presentation.adapters.WeatherDetailsListAdapter
import com.baymax.weather.forecast.weather_forecast.presentation.adapters.WeatherListAdapter
import com.baymax.weather.forecast.weather_forecast.presentation.view_model.HomeFragmentViewModel
import kotlinx.coroutines.flow.collectLatest

class HomeFragment : BaseBindingFragment<FragmentHomeBinding, HomeFragmentViewModel>(
    FragmentHomeBinding::inflate
) {
    private lateinit var linearLayoutManager: LinearLayoutManager

    private var predictionsMap = emptyMap<String, Location>()

    private var viewModel: HomeFragmentViewModel? = null

    companion object {
        private const val MULTIPLE_LOCATION_PERMISSION = 1
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            viewModel = getViewModelInstanceWithOwner(it)
        }
        binding.homeFragmentViewModel = viewModel
        setupClickListeners()
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
            val searchText = adapterView.getItemAtPosition(position).toString()
            onPredictionClick(searchText)
        }
    }

    private fun onPredictionClick(searchText: String) = lifecycleScope.launchWhenStarted {
        predictionsMap[searchText]?.let { viewModel?.updateLocationOnSearch(it) }
    }

    private fun setupObservers() {
        observeSuggestions()
        observeWeather()
        getLocationAndStartObservingWeather()
    }

    private fun observeSuggestions() = lifecycleScope.launchWhenStarted {
        binding.searchText.doAfterTextChanged { text ->
            if (!text.toString().isNullOrEmpty()) {
                viewModel?.fetchAndUpdatePredictionsList(text.toString())
            }
        }
        viewModel?.predictionsState?.collectLatest { mapOfPredictions ->
            updateListOfPredictions(mapOfPredictions)
        }
    }

    private fun updateListOfPredictions(predictions: Map<String, Location>) = with(binding) {
        predictionsMap = predictions
        searchText.setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.support_simple_spinner_dropdown_item,
                predictions.map { it.key }
            )
        )
    }

    private fun observeWeather() = lifecycleScope.launchWhenStarted {
        viewModel?.weatherState?.collectLatest { uiState ->
            when (uiState) {
                is BaseViewState.Success -> uiState.data?.let { data ->
                    setupWeatherSuccessView(data)
                } ?: setupWeatherEmptyView()

                is BaseViewState.Error -> showSnackBar(uiState.errorMessage)
                is BaseViewState.Loading -> setupWeatherLoadingView(uiState.msg)
                is BaseViewState.Empty -> setupWeatherEmptyView()
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
        location = data
        currentweather = data.dataGroupedByTime[getCurrentDateTime()["time"]]?.get(0)
        recyclerView.apply {
            layoutManager = linearLayoutManager
            adapter =
                WeatherListAdapter(data.dataGroupedByTime[getCurrentDateTime()["time"]]) { binding, date ->
                    val listGroupedByDate = data.dataGroupedByDate[date]
                    if (date.isNotBlank()) {
                        onItemClick(binding, listGroupedByDate)
                    }
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

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            MULTIPLE_LOCATION_PERMISSION
        )
    }

    private fun hasLocationPermission(): Boolean {
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

    private fun getLocationAndStartObservingWeather() {
        binding.searchText.setText("")
        when {
            hasLocationPermission() && requireContext().isGPSActive() -> viewModel?.fetchAndUpdateDeviceLocation()
            !hasLocationPermission() -> requestLocationPermission()
            else -> (activity as MainActivity).turnOnGPS()
        }
    }

    private fun onItemClick(
        binding: ItemViewWeatherDetailsBinding,
        listGroupedByDate: List<WeatherDM>?
    ) = binding.apply {
        if (!hiddenView.root.isVisible) {
            TransitionManager.beginDelayedTransition(
                cvWeatherItem,
                AutoTransition()
            )
            hiddenView.root.visibility = View.VISIBLE
            hiddenView.recyclerView.apply {
                layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    true
                )
                adapter = listGroupedByDate?.let { WeatherDetailsListAdapter(it) }
            }
            exposedView.ivExpandAnim.apply {
                setAnimation(R.raw.arrow_up)
                playAnimation()
            }
        } else {
            hiddenView.root.visibility = View.GONE
            exposedView.ivExpandAnim.apply {
                setAnimation(R.raw.arrow_down)
                playAnimation()
            }
            TransitionManager.beginDelayedTransition(
                cvWeatherItem,
                AutoTransition()
            )
        }
    }
}
