package com.baymax.weather.forecast.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.baymax.weather.forecast.R
import com.baymax.weather.forecast.api.weather_api.domain_model.WeatherDM
import com.baymax.weather.forecast.databinding.ItemViewWeatherDetailsBinding

class WeatherListAdapter(
    private val groupedByTime: List<WeatherDM>?,
    private val onItemClick: (ItemViewWeatherDetailsBinding, String) -> Unit
) : RecyclerView.Adapter<WeatherListAdapter.WeatherViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val weatherRowItemBinding: ItemViewWeatherDetailsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_view_weather_details,
            parent,
            false
        )
        return WeatherViewHolder(weatherRowItemBinding)
    }

    override fun getItemCount(): Int {
        return groupedByTime?.size ?: 0
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val weatherData = groupedByTime?.get(position)
        holder.bindData(weatherData, onItemClick)
    }

    class WeatherViewHolder(
        val binding: ItemViewWeatherDetailsBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(
            weatherData: WeatherDM?,
            onItemClick: (ItemViewWeatherDetailsBinding, String) -> Unit
        ) = binding.apply {
            data = weatherData
            exposedView.ivExpandAnim.setOnClickListener {
                onItemClick(
                    binding,
                    weatherData?.dtTxt?.split(" ")?.get(0) ?: "",
                )
            }
        }
    }
}