package com.baymax.weatherforcast.ui.fragments.home_fragment.ui

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.baymax.weatherforcast.ui.fragments.home_fragment.data.WeatherData
import com.baymax.weatherforcast.R
import com.baymax.weatherforcast.api.response.Record
import com.baymax.weatherforcast.utils.inflate
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.weather_row_item.view.*
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDateTime

class WeatherListAdapter(private val data:ArrayList<Record>, private val recentDate:String): RecyclerView.Adapter<WeatherListAdapter.WeatherViewHolder> (){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val inflatedView = parent.inflate(R.layout.weather_row_item, false)
        return WeatherViewHolder(inflatedView)
    }


    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val weather_data = data[position]
        val day = LocalDateTime.parse(recentDate.replace( " ","T")).dayOfWeek
        holder.bindData(weather_data,day)
    }

    class WeatherViewHolder(view: View): RecyclerView.ViewHolder(view) {
        companion object{
            private val Quote_Key = "QUOTE"
        }

        fun bindData(data: Record, dayOfWeek: DayOfWeek)
        {
            Picasso.get().load("https://openweathermap.org/img/wn/"+data.weather.get(0).icon+"@2x.png").centerCrop().fit().into(itemView.list_weather_icon)
            val calculted_day = LocalDateTime.parse(data.dtTxt.replace( " ","T")).dayOfWeek.toString()
            if(calculted_day.equals((dayOfWeek).toString())) {
                itemView.list_day.text = "Today"
            }
            else if(calculted_day.equals((dayOfWeek+1).toString())){
                itemView.list_day.text = "Tomorrow"
            }
            else{
                itemView.list_day.text = toStandardString(calculted_day)
            }
            itemView.list_description.setText(data.weather.get(0).description)
            itemView.list_temp.setText(Math.round((data.main.temp-273.15)).toString()+"°")
        }

        private fun toStandardString(s:String):String{
            val cap: String = s.toLowerCase().substring(0, 1).toUpperCase() + s.toLowerCase().substring(1)
            return cap
        }

    }
}