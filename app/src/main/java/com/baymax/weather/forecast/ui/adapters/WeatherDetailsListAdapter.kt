package com.baymax.weather.forecast.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.baymax.weather.forecast.R
import com.baymax.weather.forecast.api.weather_api.domain_model.WeatherDM
import com.baymax.weather.forecast.databinding.ItemViewWeatherDetailsVerticleBinding

class WeatherDetailsListAdapter(
    private val data: List<WeatherDM>
) : RecyclerView.Adapter<WeatherDetailsListAdapter.WeatherDetailsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherDetailsViewHolder {
        val weatherDetailsRowItemBinding: ItemViewWeatherDetailsVerticleBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_view_weather_details_verticle,
                parent,
                false
            )
        return WeatherDetailsViewHolder(weatherDetailsRowItemBinding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: WeatherDetailsViewHolder, position: Int) {
        val weatherData = data[position]
        holder.bindData(weatherData)
    }

    class WeatherDetailsViewHolder(val binding: ItemViewWeatherDetailsVerticleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(data: WeatherDM) {
            binding.data = data
        }
    }
}