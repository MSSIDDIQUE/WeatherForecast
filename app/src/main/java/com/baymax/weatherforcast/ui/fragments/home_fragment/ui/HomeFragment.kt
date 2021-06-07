package com.baymax.weatherforcast.ui.fragments.home_fragment.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.baymax.weatherforcast.R
import com.baymax.weatherforcast.api.response.Record
import com.baymax.weatherforcast.data.Result
import com.baymax.weatherforcast.databinding.FragmentHomeBinding
import com.baymax.weatherforcast.ui.activities.MainActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment() , KodeinAware{

    override val kodeinContext = kcontext<Fragment>(this)
    override val kodein by kodein()
    private lateinit var viewModel: HomeFramentViewModel
    private lateinit var weather_adapter: WeatherListAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var main_activity: MainActivity
    private var _binding: FragmentHomeBinding? = null
    private val binding  get() = _binding!!

    private val viewModelFactory: HomeFramentViewModelFactory by instance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity(),viewModelFactory).get(HomeFramentViewModel::class.java)
        main_activity  = (requireActivity() as MainActivity)
        main_activity.run {
            val permissions_granted = hasLocationPermission()
            val gps_active = isGPSActive()
            if(permissions_granted && gps_active){
                viewModel.setGpsStatus(true)
            }
            if(!permissions_granted){
                requestLocationPermission()
            }
            else if(!gps_active){
                turnOnGPS()
            }
        }
        if(main_activity.isOnline(requireContext())){
            viewModel.isGpsEnabled.observe(requireActivity(), Observer {
                binding.apply {
                    progressBar.visibility = View.VISIBLE
                    if(it){
                        observeData()
                    }
                }
            })
        }
        else{
            val direction = HomeFragmentDirections.actionHomeFragmentToErrorFragment(
                "No Internet Connection"
            )
            findNavController().navigate(direction)
            Snackbar.make(
                main_layout,
                "No Internet Connection",
                Snackbar.LENGTH_LONG).setAction(
                "Retry",
                View.OnClickListener {
                    findNavController().navigate(
                        R.id.action_error_fragment_to_home_fragment
                    )
                }
            ).show()
        }
    }

    fun observeData(){
        viewModel.location.observe(requireActivity(), Observer {city->
            viewModel.getWeatherOfCity(city).observe(requireActivity(), Observer {result->
                when(result.status){
                    Result.Status.SUCCESS->result.data?.let { data->
                        if(data.list.isEmpty() || data.list.size<40){
                            return@Observer
                        }
                        val recentDate = getRecentTime(data.list)
                        val recentWeatherReport = data.list.filter {
                            it.dtTxt.contains(recentDate)
                        }.get(0)
                        weather_adapter = WeatherListAdapter(data.list.filter {
                            it.dtTxt.contains(
                                recentDate.split(" ")[1]
                            )
                        } as ArrayList<Record>,recentDate)
                        linearLayoutManager = LinearLayoutManager(context)
                        binding.apply {
                            location = data.city
                            currentweather = recentWeatherReport
                            recyclerView.apply {
                                layoutManager = linearLayoutManager
                                adapter = weather_adapter
                            }
                            progressBar.visibility = View.GONE
                        }
                        /*
                        date_time.text = recentDate
                        wind_speed_value.text = data.list.get(0).wind.speed.toString()+"Kmph"
                        day.text = toStandardString(LocalDateTime.parse(recentDate.replace( " ","T")).dayOfWeek.toString())
                        recentWeatherReport[0].main.run{
                            humidity_value.text = humidity.toString()+"%"
                            temp_value.text = Math.round((temp-273.15)).toString()+"°"
                            min_temp_value.text = Math.round(tempMin-273.15).toString()+"°"
                            max_temp_value.text = Math.round((tempMax-273.15)).toString()+"°"
                        }
                        description_value.text = recentWeatherReport[0].weather[0].description
                        data.city.run{
                            location.text = data.city.name
                            sunrise_text.text = getTimeFromTimestamp(sunrise.toString())+" AM"
                            sunset_text.text = getTimeFromTimestamp(sunset.toString())+" PM"
                        }
                        */
                    }
                    Result.Status.LOADING->{
                        binding.apply {
                            progressBar.visibility = View.VISIBLE
                        }
                    }
                    Result.Status.ERROR->{
                        val direction = HomeFragmentDirections.actionHomeFragmentToErrorFragment(
                            "Something went wrong!"
                        )
                        findNavController().navigate(direction)
                        Snackbar.make(
                            requireActivity().main_layout,
                            "No Internet Connection",
                            Snackbar.LENGTH_LONG).setAction(
                            "Retry",
                            View.OnClickListener {
                                findNavController().navigate(
                                    R.id.action_error_fragment_to_home_fragment
                                )
                            }
                        ).show()
                    }
                }
            })
        })
    }

    private fun getRecentTime(list:List<Record>):String{
        val times : TreeSet<LocalDateTime> = TreeSet<LocalDateTime>()
        val current = LocalDateTime.now()
        var inputDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        var date = current.format(inputDate)
        for(it in list){
            times.add(LocalDateTime.parse(it.dtTxt.replace(" ","T")))
        }
        val recent = times.floor(LocalDateTime.parse(date.replace(" ","T")))
        var recentDateString = recent.format(inputDate)
        if(recentDateString == null) {
            return times?.first().toString().replace("T"," ")
        }
        return recentDateString?.toString().replace("T"," ")
    }

    private fun toStandardString(s:String):String{
        val cap: String = s.toLowerCase().substring(0, 1).toUpperCase() + s.toLowerCase().substring(1)
        return cap
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
