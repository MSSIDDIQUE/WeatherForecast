package com.baymax.weatherforecast.ui.fragments.home_fragment.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.baymax.weatherforecast.R
import com.baymax.weatherforecast.api.weatherApi.domainModel.WeatherDM
import com.baymax.weatherforecast.databinding.WeatherDetailsRowItemBinding

class WeatherDetailsListAdapter(
    private val data: List<WeatherDM>
) : RecyclerView.Adapter<WeatherDetailsListAdapter.WeatherDetailsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherDetailsViewHolder {
        val weatherDetailsRowItemBinding: WeatherDetailsRowItemBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.weather_details_row_item,
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

    class WeatherDetailsViewHolder(val binding: WeatherDetailsRowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(data: WeatherDM) {
            binding.data = data
        }
    }
}