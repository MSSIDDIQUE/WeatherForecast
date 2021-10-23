package com.baymax.weatherforcast.ui.fragments.home_fragment.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.switchMap
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.baymax.weatherforcast.R
import com.baymax.weatherforcast.api.response.weatherApi.Record
import com.baymax.weatherforcast.data.Result
import com.baymax.weatherforcast.databinding.FragmentHomeBinding
import com.baymax.weatherforcast.ui.activities.MainActivity
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.upper_view.*
import kotlinx.android.synthetic.main.upper_view.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class HomeFragment : DaggerFragment() {
    private lateinit var viewModel: HomeFragmentViewModel
    private lateinit var weatherListAdapter: WeatherListAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

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
        ).get(HomeFragmentViewModel::class.java)
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
                    }
                    else{
                        hidden_view.visibility = View.GONE
                        it.view_more_or_less.text = getString(R.string.view_more)
                        TransitionManager.beginDelayedTransition(
                            cardTemp,
                            AutoTransition()
                        )
                    }
                }
            }
            cardCurrentLocation.setOnClickListener {
                (activity as MainActivity).run {
                    if (hasLocationPermission() && isGPSActive()) {
                        startCollectingDeviceLocation()
                        searchText.setText("")
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
                        lifecycleScope.launchWhenStarted {
                            val result = withContext(Dispatchers.Default) {
                                getCoordinates(it)
                            }
                            when (result) {
                                is Result.Failure -> result.msg?.let { msg ->
                                    (activity as MainActivity).showSnackbar(msg)
                                }
                                is Result.Success -> {
                                    val location = result.data?.result?.geometry?.location
                                    location?.let {
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
            }.observe(viewLifecycleOwner, { result ->
                when (result) {
                    is Result.Failure -> result.msg?.let {
                        (activity as MainActivity).showSnackbar(it)
                    }
                    is Result.Loading -> {
                    }
                    is Result.Success -> {
                        val predictions = mutableListOf<String>()
                        placeIdMap.clear()
                        result.data?.predictions?.forEach {
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
            })
        }
    }

    private fun observeWeather() = lifecycleScope.launchWhenStarted {
        viewModel.run {
            weatherData.collect { result ->
                when (result) {
                    is HomeFragmentViewModel.UiState.Success -> result.wData.run {
                        binding.apply {
                            cardTemp.visibility = View.VISIBLE
                            rvContainer.visibility = View.VISIBLE
                            lineWeatherForecastContainer.visibility = View.VISIBLE
                        }
                        if (list.isEmpty() || list.size < 40) {
                            return@collect
                        }
                        val recentDate = getRecentTime(list)
                        val recentWeatherReport = list.filter {
                            it.dtTxt.contains(recentDate)
                        }[0]
                        weatherListAdapter = WeatherListAdapter(list.filter {
                            it.dtTxt.contains(
                                recentDate.split(" ")[1]
                            )
                        } as ArrayList<Record>, recentDate)
                        linearLayoutManager = LinearLayoutManager(context)
                        binding.apply {
                            location = city
                            currentweather = recentWeatherReport
                            recyclerView.apply {
                                layoutManager = linearLayoutManager
                                adapter = weatherListAdapter
                            }
                            (activity as MainActivity).progressBar.visibility = View.GONE
                        }
                    }
                    is HomeFragmentViewModel.UiState.Error -> {
                        (activity as MainActivity).showSnackbar(result.msg)
                    }
                    is HomeFragmentViewModel.UiState.Loading -> {
                        binding.apply {
                            (activity as MainActivity).progressBar.visibility = View.VISIBLE
                        }
                    }
                    is HomeFragmentViewModel.UiState.Empty -> {
                        binding.apply {
                            cardTemp.visibility = View.GONE
                            rvContainer.visibility = View.GONE
                            lineWeatherForecastContainer.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun getRecentTime(list: List<Record>): String {
        val times: TreeSet<LocalDateTime> = TreeSet<LocalDateTime>()
        val current = LocalDateTime.now()
        val inputDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val date = current.format(inputDate)
        for (it in list) {
            times.add(LocalDateTime.parse(it.dtTxt.replace(" ", "T")))
        }
        val recent = times.floor(LocalDateTime.parse(date.replace(" ", "T")))
        val recentDateString = recent.format(inputDate)
            ?: return times.first().toString().replace("T", " ")
        return recentDateString.replace("T", " ")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
