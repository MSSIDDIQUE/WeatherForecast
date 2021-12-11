package com.baymax.weatherforecast.ui.fragments.home_fragment.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.baymax.weatherforecast.databinding.BottomSheetWeatherDetailsBinding
import com.baymax.weatherforecast.ui.activities.MainActivity
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

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
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
        when (val data = viewModel.weatherData.value) {
            is HomeFragmentViewModel.UiState.Success -> {
                linearLayoutManager = LinearLayoutManager(
                    context,
                    LinearLayoutManager.HORIZONTAL,
                    true
                )
                binding.recyclerView.apply {
                    data.wData.dataGroupedByDate[date]?.let { list ->
                        adapter = WeatherDetailsListAdapter(list)
                        binding.data = list[0]
                    }
                    layoutManager = linearLayoutManager
                }
                binding.include.visibility = View.GONE
            }
            is HomeFragmentViewModel.UiState.Error -> {
                (activity as MainActivity).showSnackbar(data.msg)
            }
            else -> {
                binding.apply {
                    include.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                }

            }
        }
    }
}