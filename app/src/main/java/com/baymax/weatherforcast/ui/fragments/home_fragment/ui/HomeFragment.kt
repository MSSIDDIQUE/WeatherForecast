package com.baymax.weatherforcast.ui.fragments.home_fragment.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.switchMap
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.baymax.weatherforcast.R
import com.baymax.weatherforcast.api.response.Record
import com.baymax.weatherforcast.data.Result
import com.baymax.weatherforcast.databinding.FragmentHomeBinding
import com.baymax.weatherforcast.ui.activities.MainActivity
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.search_bar_view.*
import kotlinx.android.synthetic.main.upper_view.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment(), KodeinAware {

    override val kodeinContext = kcontext<Fragment>(this)
    override val kodein by kodein()
    private lateinit var viewModel: HomeFramentViewModel
    private lateinit var weather_adapter: WeatherListAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var actvty: Activity
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModelFactory: HomeFramentViewModelFactory by instance()
    private lateinit var countries: ArrayList<String>
    private lateinit var cities: ArrayList<String>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        actvty = requireActivity() as  MainActivity
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
        ).get(HomeFramentViewModel::class.java)
        fetchAllCities()
        search_btn.setOnClickListener {
            actvty.progressBar.visibility = View.VISIBLE
            search_text.text.toString().trim().lowercase().let { text ->
                if (text.isNotEmpty() && cities.contains(text)) {
                    search_text_layout.isErrorEnabled = false
                    viewModel.setSearchedCity(text)
                } else {
                    search_text_layout.error = getString(R.string.search_error_msg)
                    actvty.progressBar.visibility = View.GONE
                }
            }
        }
        observeData()
    }

    private fun observeData() {
        actvty.progressBar.visibility = View.VISIBLE
        viewModel.run {
            searchedCity.switchMap {
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
                            weather_adapter = WeatherListAdapter(data.list.filter {
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
                                    adapter = weather_adapter
                                }
                                actvty.progressBar.visibility = View.GONE
                            }
                        }
                    }
                    is Result.Failure -> {
                        val direction = HomeFragmentDirections.actionHomeFragmentToErrorFragment(
                            "Something went wrong!"
                        )
                        findNavController().navigate(direction)
                        Snackbar.make(
                            requireActivity().main_layout,
                            "Something went wrong!",
                            Snackbar.LENGTH_LONG
                        ).show()
                        actvty.progressBar.visibility = View.GONE
                    }
                    is Result.Loading -> {
                        binding.apply {
                            actvty.progressBar.visibility = View.VISIBLE
                        }
                    }
                }
            })
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

    private fun fetchAllCities() {
        val array: HashMap<String, ArrayList<String>> = Gson().fromJson(
            getJsonData(),
            object :
                TypeToken<HashMap<String?, ArrayList<String?>?>?>() {}.type
        )
        countries = ArrayList(array.keys)
        cities = getCities(array)
    }

    private fun getJsonData(): String? {
        lateinit var json: String
        try {
            val inputStream: InputStream = requireActivity().assets.open("cities.json")
            json = inputStream.bufferedReader().use { it.readText() }
        } catch (ex: Exception) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    private fun getCities(m: Map<String, ArrayList<String>>): ArrayList<String> {
        val cities = ArrayList<String>()
        for ((_, v) in m) {
            v.forEach {
                cities.add(it.lowercase())
            }
        }
        return cities
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
