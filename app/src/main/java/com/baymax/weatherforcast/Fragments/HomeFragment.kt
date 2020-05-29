package com.baymax.weatherforcast.Fragments

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.baymax.weatherforcast.Model.Network.ConnectivityInterceptorImpl
import com.baymax.weatherforcast.Model.Network.WeatherNetworkAbstractionsImpl
import com.baymax.weatherforcast.Model.WeatherApiService

import com.baymax.weatherforcast.R
import com.baymax.weatherforcast.ViewModel.HomeFramentViewModel
import com.baymax.weatherforcast.ViewModel.HomeFramentViewModelFactory
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext


class HomeFragment : Fragment() , KodeinAware{

    override val kodeinContext = kcontext<Fragment>(this)
    override val kodein by kodein()
    private lateinit var viewModel:HomeFramentViewModel

    private val viewModelFactory:HomeFramentViewModelFactory by instance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val weatherApiService = WeatherApiService(ConnectivityInterceptorImpl(this.context!!))
        val weatherNetworkAbstractions = WeatherNetworkAbstractionsImpl(weatherApiService)
//        weatherNetworkAbstractions.downloadedWeather.observe(this@HomeFragment, Observer {
//            location.text = it.city.name
//            date_time.text = it.list.get(0).dtTxt
//            var inputDate = SimpleDateFormat("yyyy-MM-dd")
//            var date = inputDate.parse(it.list.get(0).dtTxt)
//            var outputDate = SimpleDateFormat("EEEE")
//            day.text = outputDate.format(date)
//            humidity_value.text = it.list.get(0).main.humidity.toString()+"%"
//            wind_speed_value.text = it.list.get(0).wind.speed.toString()+"Kmph"
//            temp.text = Math.round((it.list.get(0).main.temp-273.15)).toString()+"°"
//            min_temp_value.text = Math.round((it.list.get(0).main.tempMin-273.15)).toString()+"°"
//            max_temp_value.text = Math.round((it.list.get(0).main.tempMax-273.15)).toString()+"°"
//            description.text = it.list.get(0).weather.get(0).description
//        })
//        GlobalScope.launch(Dispatchers.Main) {
//            RepositoryImpl(MyDatabase(context!!).weatherDataDao(),weatherNetworkAbstractions,
//                RecordMapperImpl(), WeatherMapperImpl()
//            )
//        }
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
            location.text = "Hyderabad"
            date_time.text = it.date_time
            var inputDate = SimpleDateFormat("yyyy-MM-dd")
            var date = inputDate.parse(it.date_time)
            var outputDate = SimpleDateFormat("EEEE")
            day.text = outputDate.format(date)
            humidity_value.text = it.humidity.toString()+"%"
            wind_speed_value.text = it.wind_speed.toString()+"Kmph"
            temp.text = Math.round((it.temp-273.15)).toString()+"°"
            min_temp_value.text = Math.round((it.min_temp-273.15)).toString()+"°"
            max_temp_value.text = Math.round((it.max_temp-273.15)).toString()+"°"
            description.text = it.weather_desc
        })

    }

}
