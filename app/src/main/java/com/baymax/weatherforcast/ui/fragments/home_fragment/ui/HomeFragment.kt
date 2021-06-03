package com.baymax.weatherforcast.ui.fragments.home_fragment.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.baymax.weatherforcast.R
import com.baymax.weatherforcast.api.response.Record
import com.baymax.weatherforcast.data.Result
import com.baymax.weatherforcast.ui.activities.MainActivity
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
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

    private val viewModelFactory: HomeFramentViewModelFactory by instance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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
                progressBar.visibility = View.VISIBLE
                loading_text.visibility = View.VISIBLE
                if(it){
                    observeData()
                }
            })
        }
        else{
            val direction = HomeFragmentDirections.actionHomeFragmentToErrorFragment(
                "No Internet Connection"
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

    private fun loadIcons(){
        Picasso.get().load(R.drawable.icons_sunrise_50).centerCrop().fit().into(sunrise_icon)
        Picasso.get().load(R.drawable.icons_sunset_50).centerCrop().fit().into(sunset_icon)
        Picasso.get().load(R.drawable.icons_dew_point_50).centerCrop().fit().into(humidity_icon)
        Picasso.get().load(R.drawable.icons_wind_50).centerCrop().fit().into(wind_speed_icon)
    }

    fun observeData(){
        viewModel.location.observe(requireActivity(), Observer {city->
            viewModel.getWeatherOfCity(city).observe(requireActivity(), Observer {result->
                when(result.status){
                    Result.Status.SUCCESS->result.data?.let { data->
                        if(data.list.isEmpty() || data.list.size<40){
                            return@Observer
                        }
                        loadIcons()
                        val recentDate = getRecentTime(data.list)
                        val recentWeatherReport = data.list.filter { it.dtTxt.contains(recentDate) }
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
                        weather_adapter = WeatherListAdapter(data.list.filter {
                            it.dtTxt.contains(recentDate.split(" ")[1])
                        } as ArrayList<Record>,recentDate)
                        linearLayoutManager = LinearLayoutManager(context)
                        recycler_view.apply {
                            layoutManager = linearLayoutManager
                            adapter = weather_adapter
                        }
                        progressBar.visibility = View.GONE
                        loading_text.visibility = View.GONE
                    }
                    Result.Status.LOADING->{
                        progressBar.visibility = View.VISIBLE
                        loading_text.visibility = View.VISIBLE
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

    private fun getTimeFromTimestamp(s: String): String? {
        try {
            val sdf = SimpleDateFormat("hh:mm")
            val netDate = Date(s.toLong()*1000)
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }

    private fun toStandardString(s:String):String{
        val cap: String = s.toLowerCase().substring(0, 1).toUpperCase() + s.toLowerCase().substring(1)
        return cap
    }
}