package com.baymax.weatherforcast.Fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.baymax.weatherforcast.Adapter.WeatherListAdapter
import com.baymax.weatherforcast.Model.DB.WeatherData
import com.baymax.weatherforcast.R
import com.baymax.weatherforcast.Utils.locations
import com.baymax.weatherforcast.ViewModel.HomeFramentViewModel
import com.baymax.weatherforcast.ViewModel.HomeFramentViewModelFactory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment() , KodeinAware{

    override val kodeinContext = kcontext<Fragment>(this)
    override val kodein by kodein()
    private lateinit var viewModel:HomeFramentViewModel
    private lateinit var adapter: WeatherListAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    private val viewModelFactory:HomeFramentViewModelFactory by instance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this,viewModelFactory).get(HomeFramentViewModel::class.java)
        bindUI()
    }

    private fun bindUI() = GlobalScope.launch(Dispatchers.Main) {

        val weatherReport = viewModel.weatherData
        weatherReport.observe(this@HomeFragment, Observer {
            if(it == null) return@Observer
            Picasso.get().load(R.drawable.icons_sunrise_50).centerCrop().fit().into(sunrise_icon)
            Picasso.get().load(R.drawable.icons_sunset_50).centerCrop().fit().into(sunset_icon)
            Picasso.get().load(R.drawable.icons_dew_point_50).centerCrop().fit().into(humidity_icon)
            Picasso.get().load(R.drawable.icons_wind_50).centerCrop().fit().into(wind_speed_icon)
            val recentDate = getRecentTime(it)
            val recentWeatherReport = it.filter { it.date_time.contains(recentDate) }
            date_time.text = recentDate
            day.text = LocalDateTime.parse(recentDate.replace( " ","T")).dayOfWeek.toString()
            humidity_value.text = recentWeatherReport[0].humidity.toString()+"%"
            wind_speed_value.text = recentWeatherReport[0].wind_speed.toString()+"Kmph"
            temp.text = Math.round((recentWeatherReport[0].temp-273.15)).toString()+"°"
            min_temp_value.text = Math.round((recentWeatherReport[0].min_temp-273.15)).toString()+"°"
            max_temp_value.text = Math.round((recentWeatherReport[0].max_temp-273.15)).toString()+"°"
            description.text = recentWeatherReport[0].weather_desc
            location.text = it.get(0).name
            sunrise_text.text = getTimeFromTimestamp(it.get(0).sunrise.toString())+" AM"
            sunset_text.text = getTimeFromTimestamp(it.get(0).sunset.toString())+" PM"
            adapter = WeatherListAdapter(it.filter { it.date_time.contains(recentDate.split(" ")[1]) } as ArrayList<WeatherData>,recentDate)
            linearLayoutManager = LinearLayoutManager(context)
            recycler_view.layoutManager = linearLayoutManager
            recycler_view.adapter = adapter
            progressBar.visibility = View.GONE
        })
    }

    private fun getRecentTime(list:List<WeatherData>):String{
        val times : TreeSet<LocalDateTime> = TreeSet<LocalDateTime>()
        val current = LocalDateTime.now()
        Log.d("(Saquib)","The current date before formatting is "+current.toString())
        var inputDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        var date = current.format(inputDate)
        Log.d("(Saquib)", "The current date after formatting is "+date.toString())
        for(it in list){
            times.add(LocalDateTime.parse(it.date_time.replace(" ","T")))
        }
        Log.d("(Saquib)","Size of the treeset is "+times.size)
        val recent = times.floor(LocalDateTime.parse(date.replace(" ","T")))
        if(recent == null) {
            return times?.first().toString().replace("T"," ")
        }
        return recent?.toString().replace("T"," ")
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


}
