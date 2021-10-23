package com.baymax.weatherforecast.ui.fragments.home_fragment.ui

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.baymax.weatherforecast.R
import com.baymax.weatherforecast.api.response.weatherApi.Record
import com.baymax.weatherforecast.utils.inflate
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.weather_row_item.view.*
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDateTime
import kotlin.math.roundToInt

class WeatherListAdapter(
    private val data: ArrayList<Record>,
    private val recentDate: String
) : RecyclerView.Adapter<WeatherListAdapter.WeatherViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val inflatedView = parent.inflate(R.layout.weather_row_item, false)
        return WeatherViewHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val weather_data = data[position]
        val day = LocalDateTime.parse(recentDate.replace(" ", "T")).dayOfWeek
        holder.bindData(weather_data, day)
    }

    class WeatherViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bindData(data: Record, dayOfWeek: DayOfWeek) {
            Picasso.get()
                .load("https://openweathermap.org/img/wn/" + data.weather.get(0).icon + "@2x.png")
                .centerCrop().fit().into(itemView.list_weather_icon)
            val today = LocalDateTime.parse(data.dtTxt.replace(" ", "T")).dayOfWeek.toString()
            when (today) {
                dayOfWeek.toString() -> itemView.list_day.text = "Today"
                (dayOfWeek + 1).toString() -> itemView.list_day.text = "Tomorrow"
                else -> itemView.list_day.text = toStandardString(today)
            }
            itemView.list_description.text = data.weather.get(0).description
            itemView.list_temp.text = (data.main.temp - 273.15).roundToInt().toString() + "°"
            itemView.max_min_list_temp.text = (data.main.tempMax - 273.15).roundToInt()
                .toString() + "°/ " + (data.main.tempMin - 273.15).roundToInt().toString() + "°"
        }

        private fun toStandardString(s: String): String {
            return s[0].uppercase() + s.substring(1).lowercase()
        }
    }
}