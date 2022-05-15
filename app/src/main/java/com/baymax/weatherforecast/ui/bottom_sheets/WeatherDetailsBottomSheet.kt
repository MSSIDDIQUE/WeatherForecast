package com.baymax.weatherforecast.ui.bottom_sheets

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.baymax.weatherforecast.api.weather_api.domain_model.ApiResponseDM
import com.baymax.weatherforecast.data.UiState
import com.baymax.weatherforecast.databinding.BottomSheetWeatherDetailsBinding
import com.baymax.weatherforecast.ui.adapters.WeatherDetailsListAdapter
import com.baymax.weatherforecast.ui.listeners.BaseEventListener
import com.baymax.weatherforecast.ui.view_model.HomeFragmentViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class WeatherDetailsBottomSheet : BottomSheetDialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var _binding: BottomSheetWeatherDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeFragmentViewModel
    private lateinit var linearLayoutManager: LinearLayoutManager
    val args by navArgs<WeatherDetailsBottomSheetArgs>()
    var listener: BaseEventListener? = null

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
        try {
            listener = context as BaseEventListener
        } catch (e: IllegalStateException) {
            Log.d("TAG", "MainActivity must implement BaseEventListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetWeatherDetailsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(
            requireActivity(),
            viewModelFactory
        )[HomeFragmentViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val date = args.date
        binding.apply {
            when (val data = viewModel.weatherData.value) {
                is UiState.Success<*> -> {
                    linearLayoutManager = LinearLayoutManager(
                        context,
                        LinearLayoutManager.HORIZONTAL,
                        true
                    )
                    recyclerView.apply {
                        (data.wData as ApiResponseDM).dataGroupedByDate[date]?.let { list ->
                            adapter = WeatherDetailsListAdapter(list)
                            binding.data = list[0]
                        }
                        layoutManager = linearLayoutManager
                    }
                    include.groupProgressBar.visibility = View.GONE
                }
                is UiState.Error -> {
                    listener?.showSnackBar(data.msg)
                }
                else -> {
                    include.groupProgressBar.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                }
            }
        }
    }
}