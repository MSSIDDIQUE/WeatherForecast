package com.baymax.weatherforcast.Fragments

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.baymax.weatherforcast.Model.Network.ConnectivityInterceptorImpl
import com.baymax.weatherforcast.Model.Network.WeatherNetworkAbstractionsImpl
import com.baymax.weatherforcast.Model.WeatherApiService

import com.baymax.weatherforcast.R
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.logging.SimpleFormatter


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val weatherApiService = WeatherApiService(ConnectivityInterceptorImpl(this.context!!))
        val weatherNetworkAbstractions = WeatherNetworkAbstractionsImpl(weatherApiService)
        weatherNetworkAbstractions.downloadedWeather.observe(this@HomeFragment, Observer {
            location.text = it.city.name
            date_time.text = it.list.get(0).dtTxt
            var inputDate = SimpleDateFormat("yyyy-MM-dd")
            var date = inputDate.parse(it.list.get(0).dtTxt)
            var outputDate = SimpleDateFormat("EEEE")
            day.text = outputDate.format(date)
            humidity_value.text = it.list.get(0).main.humidity.toString()+"%"
            wind_speed_value.text = it.list.get(0).wind.speed.toString()+"Kmph"
            temp.text = Math.round((it.list.get(0).main.temp-273.15)).toString()+"°"
            min_temp_value.text = Math.round((it.list.get(0).main.tempMin-273.15)).toString()+"°"
            max_temp_value.text = Math.round((it.list.get(0).main.tempMax-273.15)).toString()+"°"
        })
        GlobalScope.launch(Dispatchers.Main) {
            weatherNetworkAbstractions.fetchWeather("Hyderabad")
        }
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

}
