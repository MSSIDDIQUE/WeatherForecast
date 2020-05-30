package com.baymax.weatherforcast.Fragments

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.baymax.weatherforcast.Adapter.WeatherListAdapter
import com.baymax.weatherforcast.Model.DB.WeatherData
import com.baymax.weatherforcast.R
import com.baymax.weatherforcast.ViewModel.HomeFramentViewModel
import com.baymax.weatherforcast.ViewModel.HomeFramentViewModelFactory
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
import org.threeten.bp.format.*
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
        val weather = viewModel.weatherData.observe(this@HomeFragment, Observer {
            if(it == null) return@Observer
            val recentDate = getRecentTime(it)
            val recentWeatherReport = it.filter { it.date_time.contains(recentDate) }
            Log.d("Saquib",recentWeatherReport.size.toString())
            location.text = "Hyderabad"
            date_time.text = recentDate
            day.text = LocalDateTime.parse(recentDate.replace( " ","T")).dayOfWeek.toString()
            humidity_value.text = recentWeatherReport[0].humidity.toString()+"%"
            wind_speed_value.text = recentWeatherReport[0].wind_speed.toString()+"Kmph"
            temp.text = Math.round((recentWeatherReport[0].temp-273.15)).toString()+"°"
            min_temp_value.text = Math.round((recentWeatherReport[0].min_temp-273.15)).toString()+"°"
            max_temp_value.text = Math.round((recentWeatherReport[0].max_temp-273.15)).toString()+"°"
            description.text = recentWeatherReport[0].weather_desc
            Picasso.get().load(recentWeatherReport[0].weather_icon).centerCrop().fit().into(weather_icon)
            adapter = WeatherListAdapter(it.filter { it.date_time.contains(recentDate.split(" ")[1]) } as ArrayList<WeatherData>,recentDate)
            linearLayoutManager = LinearLayoutManager(context)
            recycler_view.layoutManager = linearLayoutManager
            recycler_view.adapter = adapter
        })
    }

    private fun getRecentTime(list:List<WeatherData>):String{
        val times : TreeSet<LocalDateTime> = TreeSet<LocalDateTime>()
        val current = LocalDateTime.now()
        var inputDate = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")
        var date = current.format(inputDate)
        for(it in list){
            times.add(LocalDateTime.parse(it.date_time.replace(" ", "T")))
        }
        val recect = times.floor(LocalDateTime.parse(date.replace(" ","T")))
        return recect.toString().replace("T"," ")
    }
}
