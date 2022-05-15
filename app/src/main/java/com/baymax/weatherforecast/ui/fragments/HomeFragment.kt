package com.baymax.weatherforecast.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.switchMap
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.baymax.weatherforecast.R
import com.baymax.weatherforecast.api.weather_api.domain_model.ApiResponseDM
import com.baymax.weatherforecast.data.Result
import com.baymax.weatherforecast.data.UiState
import com.baymax.weatherforecast.databinding.FragmentHomeBinding
import com.baymax.weatherforecast.ui.activities.MainActivity
import com.baymax.weatherforecast.ui.view_model.HomeFragmentViewModel
import com.baymax.weatherforecast.ui.adapters.WeatherListAdapter
import com.baymax.weatherforecast.utils.isGPSActive
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.text.DecimalFormat
import kotlin.collections.HashMap

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
                        val result = withContext(Dispatchers.Default) {
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
        viewModel.setUiState(UiState.Loading(getString(R.string.collecting_location)))
        viewModel.startCollectingDeviceLocation()
    }

    private fun observeSuggestions() {
        viewModel.run {
            searchText.switchMap {
                getSuggestions(it)
            }.observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Result.Failure -> result.msg?.let {
                        showSnackBar(it)
                    }
                    is Result.Success -> {
                        val predictions = mutableListOf<String>()
                        placeIdMap.clear()
                        result.data.predictions.forEach {
                            predictions.add(it.description)
                            placeIdMap[it.description] = it.placeId
                        }
                        val adapter = ArrayAdapter(
                            requireContext(),
                            R.layout.support_simple_spinner_dropdown_item,
                            predictions
                        )
                        binding.searchText.setAdapter(adapter)
                    }
                }
            }
        }
    }

    private fun observeWeather() = binding.apply {
        viewModel.weatherData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is UiState.Success<*> -> (result.wData as ApiResponseDM).run {
                    cardTemp.visibility = View.VISIBLE
                    rvContainer.visibility = View.VISIBLE
                    lineWeatherForecastContainer.visibility = View.VISIBLE
                    if (dataGroupedByDate.isEmpty()) {
                        return@observe
                    }
                    val currentDateTime = getCurrentDataTime()
                    linearLayoutManager = LinearLayoutManager(context)
                    location = city
                    dataGroupedByTime[currentDateTime["time"]]?.let { recentWeatherReport ->
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
                is UiState.Error -> {
                    showSnackBar(result.msg)
                }
                is UiState.Loading -> {
                    showProgressBar(true, result.msg)
                    cardTemp.visibility = View.GONE
                    rvContainer.visibility = View.GONE
                    lineWeatherForecastContainer.visibility = View.GONE
                }
                is UiState.Empty -> {
                    showProgressBar(false)
                    cardTemp.visibility = View.GONE
                    rvContainer.visibility = View.GONE
                    lineWeatherForecastContainer.visibility = View.GONE
                }
            }
        }
    }

    private fun getCurrentDataTime(): HashMap<String, String> {
        val dateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formattedDateTime = dateTime.format(formatter).split(" ")
        var hourInTime = formattedDateTime[1].subSequence(0, 2).toString().toInt()
        hourInTime -= hourInTime % 3
        return hashMapOf<String, String>().apply {
            this["date"] = formattedDateTime[0]
            this["time"] = "${DecimalFormat("00").format(hourInTime)}:00:00"
        }
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
            hasLocationPermission() && requireContext().isGPSActive() -> viewModel.setUiState(
                UiState.Loading(getString(R.string.collecting_location))
            )
            !hasLocationPermission() -> requestLocationPermission()
            else -> (activity as MainActivity).turnOnGPS()
        }
    }
}
