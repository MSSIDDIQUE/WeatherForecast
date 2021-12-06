package com.baymax.weatherforecast.ui.fragments.home_fragment.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.baymax.weatherforecast.R
import com.baymax.weatherforecast.api.weatherApi.domainModel.WeatherDM
import com.baymax.weatherforecast.databinding.WeatherRowItemBinding

class WeatherListAdapter(
    private val data: List<WeatherDM>
) : RecyclerView.Adapter<WeatherListAdapter.WeatherViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val weatherRowItemBinding: WeatherRowItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.weather_row_item,
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
        holder.bindData(weatherData)
    }

    class WeatherViewHolder(val binding: WeatherRowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(data: WeatherDM) {
            binding.data = data
        }
    }
}