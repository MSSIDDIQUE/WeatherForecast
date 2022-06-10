package com.baymax.weatherforecast.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.baymax.weatherforecast.R
import com.baymax.weatherforecast.api.weather_api.domain_model.WeatherDM
import com.baymax.weatherforecast.databinding.ItemViewWeatherDetailsHorizontalBinding

class WeatherListAdapter(
    private val data: List<WeatherDM>,
    private val listener: WeatherDetailsItemListener
) : RecyclerView.Adapter<WeatherListAdapter.WeatherViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val weatherRowItemBinding: ItemViewWeatherDetailsHorizontalBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_view_weather_details_horizontal,
            parent,
            false
        )
        return WeatherViewHolder(weatherRowItemBinding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val weatherData = data[position]
        holder.bindData(weatherData, listener)
    }

    class WeatherViewHolder(val binding: ItemViewWeatherDetailsHorizontalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(data: WeatherDM, listener: WeatherDetailsItemListener) {
            binding.data = data
            binding.listener = listener
        }
    }

    interface WeatherDetailsItemListener {
        fun onItemClick(date: String)
    }
}