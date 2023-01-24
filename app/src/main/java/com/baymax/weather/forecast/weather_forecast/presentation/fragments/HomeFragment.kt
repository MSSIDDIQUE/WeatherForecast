package com.baymax.weather.forecast.weather_forecast.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.baymax.weather.forecast.R
import com.baymax.weather.forecast.databinding.FragmentHomeBinding
import com.baymax.weather.forecast.databinding.ItemViewWeatherDetailsBinding
import com.baymax.weather.forecast.fetch_location.api.data_transfer_model.Location
import com.baymax.weather.forecast.presentation.fragments.BaseBindingFragment
import com.baymax.weather.forecast.presentation.view_state.BaseViewState
import com.baymax.weather.forecast.presentation.view_state.ProgressBarViewState
import com.baymax.weather.forecast.presentation.view_state.SnackBarViewState
import com.baymax.weather.forecast.utils.DateTimeUtils.getCurrentDateTime
import com.baymax.weather.forecast.weather_forecast.api.domain_model.ApiResponseDM
import com.baymax.weather.forecast.weather_forecast.api.domain_model.WeatherDM
import com.baymax.weather.forecast.weather_forecast.presentation.adapters.WeatherDetailsListAdapter
import com.baymax.weather.forecast.weather_forecast.presentation.adapters.WeatherListAdapter
import com.baymax.weather.forecast.weather_forecast.presentation.listeners.HomeFragmentEventListener
import com.baymax.weather.forecast.weather_forecast.presentation.view_model.HomeFragmentViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : BaseBindingFragment<FragmentHomeBinding, HomeFragmentViewModel>(
    FragmentHomeBinding::inflate
) {
    private lateinit var linearLayoutManager: LinearLayoutManager

    private var predictionsMap = mutableMapOf<String, String>()

    private var predictionsAdapter: ArrayAdapter<String>? = null

    private var viewModel: HomeFragmentViewModel? = null

    private var eventListener: HomeFragmentEventListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        eventListener = context as HomeFragmentEventListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let { viewModel = getViewModelInstanceWithOwner(it) }
        binding.homeFragmentViewModel = viewModel
        setupClickListeners()
        setupPredictionsAdapter()
        setupObservers()
    }

    private fun setupClickListeners() = with(binding) {
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
            binding.searchText.setText("")
            eventListener?.updateCurrentDeviceLocation()
        }
        searchText.setOnItemClickListener { adapterView, _, position, _ ->
            val searchText = adapterView.getItemAtPosition(position).toString()
            onPredictionClick(searchText)
        }
    }

    private fun onPredictionClick(searchText: String) = lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            predictionsMap[searchText]?.let {
                viewModel?.updateLocationFromPlaceId(it)
                predictionsMap.clear()
            }
        }
    }

    private fun setupObservers() {
        observeSuggestions()
        observeWeather()
    }

    private fun observeSuggestions() {
        binding.searchText.doAfterTextChanged { searchQuery ->
            if (searchQuery.toString().isNotEmpty()) {
                viewModel?.searchQuery?.value = searchQuery.toString()
            }
        }
        lifecycleScope.launch {
            viewModel?.predictions?.collectLatest { latestPredictions ->
                predictionsAdapter?.run {
                    latestPredictions?.let { predictions ->
                        addAll(predictions.keys)
                        predictionsMap.putAll(predictions)
                    } ?: clear()
                    notifyDataSetChanged()
                }
            }
        }
    }

    private fun setupPredictionsAdapter() = with(binding) {
        predictionsAdapter = ArrayAdapter(
            requireContext(),
            R.layout.support_simple_spinner_dropdown_item,
            emptyMap<String, Location>().map { it.key }
        ).also { searchText.setAdapter(it) }
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
        viewModel?.setProgressBarState(ProgressBarViewState.Hide)
    }

    private fun setupWeatherEmptyView() = with(binding) {
        viewModel?.setProgressBarState(ProgressBarViewState.Hide)
        cardTemp.visibility = View.GONE
        rvContainer.visibility = View.GONE
        lineWeatherForecastContainer.visibility = View.GONE
    }

    private fun setupWeatherLoadingView(msg: String) = with(binding) {
        viewModel?.setProgressBarState(ProgressBarViewState.Show(msg))
        cardTemp.visibility = View.GONE
        rvContainer.visibility = View.GONE
        lineWeatherForecastContainer.visibility = View.GONE
    }

    private fun onItemClick(
        binding: ItemViewWeatherDetailsBinding,
        listGroupedByDate: List<WeatherDM>?
    ) = with(binding) {
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
