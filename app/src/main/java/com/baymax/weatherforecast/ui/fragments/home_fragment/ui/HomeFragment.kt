package com.baymax.weatherforecast.ui.fragments.home_fragment.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.baymax.weatherforecast.R
import com.baymax.weatherforecast.data.Result
import com.baymax.weatherforecast.databinding.FragmentHomeBinding
import com.baymax.weatherforecast.ui.activities.MainActivity
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.progress_bar_view.view.*
import kotlinx.android.synthetic.main.upper_view.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.text.DecimalFormat
import javax.inject.Inject
import kotlin.collections.HashMap


class HomeFragment : DaggerFragment(), WeatherListAdapter.WeatherDetailsItemListener {
    private lateinit var viewModel: HomeFragmentViewModel
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    companion object {
        const val SELECTED_ITEM_DATE = "date"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(
            requireActivity(),
            viewModelFactory
        )[HomeFragmentViewModel::class.java]
        binding.apply {
            homeFragmentViewModel = viewModel
        }
        setupClickListeners()
        setupObservers()
    }

    private fun setupClickListeners() {
        binding.apply {
            cardTemp.apply {
                view_more_or_less_container.setOnClickListener {
                    if (!hidden_view.isVisible) {
                        TransitionManager.beginDelayedTransition(
                            cardTemp,
                            AutoTransition()
                        )
                        hidden_view.visibility = View.VISIBLE
                        it.view_more_or_less.text = getString(R.string.view_less)
                    } else {
                        hidden_view.visibility = View.GONE
                        it.view_more_or_less.text = getString(R.string.view_more)
                        TransitionManager.beginDelayedTransition(
                            cardTemp,
                            AutoTransition()
                        )
                    }
                }
            }
            btnUseMyLocation.setOnClickListener {
                searchText.setText("")
                (activity as MainActivity).run {
                    if (hasLocationPermission() && isGPSActive()) {
                        startCollectingDeviceLocation()
                    } else if (!hasLocationPermission()) {
                        requestLocationPermission()
                    } else
                        turnOnGPS()
                }
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
                                    (activity as MainActivity).showSnackbar(msg)
                                }
                                is Result.Success -> {
                                    val location = result.data.result.geometry.location
                                    location.let {
                                        if (!(activity as MainActivity).no_internet_background.isVisible) {
                                            getWeather(it)
                                        } else {
                                            (activity as MainActivity).showSnackbar(getString(R.string.no_internet_connection))
                                        }
                                    }
                                }
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
    }

    private fun observeSuggestions() {
        viewModel.run {
            searchText.switchMap {
                getSuggestions(it)
            }.observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Result.Failure -> result.msg?.let {
                        (activity as MainActivity).showSnackbar(it)
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

    private fun observeWeather() = viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.run {
                weatherData.collect { result ->
                    when (result) {
                        is HomeFragmentViewModel.UiState.Success -> result.wData.run {
                            binding.apply {
                                cardTemp.visibility = View.VISIBLE
                                rvContainer.visibility = View.VISIBLE
                                lineWeatherForecastContainer.visibility = View.VISIBLE
                            }
                            if (dataGroupedByDate.isEmpty()) {
                                return@collect
                            }
                            val currentDateTime = getCurrentDataTime()
                            linearLayoutManager = LinearLayoutManager(context)
                            binding.apply {
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
                                (activity as MainActivity).progressBar.visibility = View.GONE
                            }
                        }
                        is HomeFragmentViewModel.UiState.Error -> {
                            (activity as MainActivity).showSnackbar(result.msg)
                        }
                        is HomeFragmentViewModel.UiState.Loading -> {
                            binding.apply {
                                (activity as MainActivity).progressBar.apply {
                                    visibility = View.VISIBLE
                                    loading_text.text = result.msg
                                }
                            }
                        }
                        is HomeFragmentViewModel.UiState.Empty -> {
                            binding.apply {
                                (activity as MainActivity).progressBar.apply {
                                    visibility = View.GONE
                                }
                                cardTemp.visibility = View.GONE
                                rvContainer.visibility = View.GONE
                                lineWeatherForecastContainer.visibility = View.GONE
                            }
                        }
                    }
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemClick(date: String) {
        val direction = HomeFragmentDirections
            .actionHomeFragmentToWeatherDetailsBottomSheet()
            .setDate(date.split(" ")[0])
        findNavController().navigate(direction)
    }
}
