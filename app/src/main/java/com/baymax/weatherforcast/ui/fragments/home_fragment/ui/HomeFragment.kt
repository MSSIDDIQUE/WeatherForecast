package com.baymax.weatherforcast.ui.fragments.home_fragment.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.switchMap
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.baymax.weatherforcast.R
import com.baymax.weatherforcast.api.response.weatherApi.Record
import com.baymax.weatherforcast.data.Result
import com.baymax.weatherforcast.databinding.FragmentHomeBinding
import com.baymax.weatherforcast.ui.activities.MainActivity
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.upper_view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
    private lateinit var actvty: Activity
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var countries: ArrayList<String>
    private lateinit var cities: ArrayList<String>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        actvty = requireActivity() as MainActivity
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
        ).get(HomeFragmentViewModel::class.java)
        binding.homeFragmentViewModel = viewModel
        binding.searchText.setOnItemClickListener { adapterView, view, position, id ->
            val selectedLocation = adapterView.getItemAtPosition(position).toString()
            viewModel.run {
                placeIdMap[selectedLocation]?.let {
                    lifecycleScope.launch {
                        val result = withContext(Dispatchers.Default) {
                            getCoordinates(it)
                        }
                        when (result) {
                            is Result.Failure -> result.msg?.let { it1 -> showErrorMsg(it1) }
                            is Result.Loading -> {
                            }
                            is Result.Success -> {
                                val location = result.data?.result?.geometry?.location
                                location?.let {
                                    mutableLocation.postValue(location)
                                }
                            }
                        }
                    }
                }
            }
        }
        observeWeather()
        observeSearchText()
    }

    private fun observeWeather() {
        actvty.progressBar.visibility = View.VISIBLE
        viewModel.run {
            mutableLocation.switchMap {
                getWeather(it)
            }.observe(viewLifecycleOwner, Observer { result ->
                when (result) {
                    is Result.Success -> {
                        result.data?.let { data ->
                            if (data.list.isEmpty() || data.list.size < 40) {
                                return@Observer
                            }
                            val recentDate = getRecentTime(data.list)
                            val recentWeatherReport = data.list.filter {
                                it.dtTxt.contains(recentDate)
                            }[0]
                            weatherListAdapter = WeatherListAdapter(data.list.filter {
                                it.dtTxt.contains(
                                    recentDate.split(" ")[1]
                                )
                            } as ArrayList<Record>, recentDate)
                            linearLayoutManager = LinearLayoutManager(context)
                            binding.apply {
                                location = data.city
                                currentweather = recentWeatherReport
                                recyclerView.apply {
                                    layoutManager = linearLayoutManager
                                    adapter = weatherListAdapter
                                }
                                actvty.progressBar.visibility = View.GONE
                            }
                        }
                    }
                    is Result.Failure -> result.msg?.let { showErrorMsg(it) }
                    is Result.Loading -> {
                        binding.apply {
                            actvty.progressBar.visibility = View.VISIBLE
                        }
                    }
                }
            })
        }
    }

    private fun observeSearchText() {
        viewModel.apply {
            searchText.switchMap {
                getSuggestions(it)
            }.observe(viewLifecycleOwner, Observer { result ->
                when (result) {
                    is Result.Failure -> result.msg?.let { showErrorMsg(it) }
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

    private fun showErrorMsg(errorMsg: String) {
        val direction = HomeFragmentDirections.actionHomeFragmentToErrorFragment(
            "Something went wrong! \n  $errorMsg "
        )
        findNavController().navigate(direction)
        Snackbar.make(
            requireActivity().main_layout,
            "Something went wrong! ($errorMsg)",
            Snackbar.LENGTH_LONG
        ).show()
        actvty.progressBar.visibility = View.GONE
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
