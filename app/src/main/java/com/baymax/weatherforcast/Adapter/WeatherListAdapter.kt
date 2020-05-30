package com.baymax.weatherforcast.Adapter

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.baymax.weatherforcast.Model.DB.WeatherData
import com.baymax.weatherforcast.R
import com.baymax.weatherforcast.Utils.inflate
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.weather_row_item.view.*
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDateTime

class WeatherListAdapter(private val data:ArrayList<WeatherData>, private val recentDate:String): RecyclerView.Adapter<WeatherListAdapter.WeatherViewHolder> (){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {

        val inflatedView = parent.inflate(R.layout.weather_row_item, false)
        return WeatherViewHolder(inflatedView)
    }


    override fun getItemCount(): Int {
        Log.d("###","toatat no of items in the list is "+data.size)
        return data.size
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val weather_data = data[position]
        val day = LocalDateTime.parse(recentDate.replace( " ","T")).dayOfWeek
        Log.d("(Saquib)", "the Day is "+day.toString())
        holder.bindData(weather_data,day)
    }

    class WeatherViewHolder(view: View): RecyclerView.ViewHolder(view) {
        companion object{
            private val Quote_Key = "QUOTE"
        }

        fun bindData(data:WeatherData,dayOfWeek: DayOfWeek)
        {
            Picasso.get().load(data.weather_icon).centerCrop().fit().into(itemView.list_weather_icon)
            val calculted_day = LocalDateTime.parse(data.date_time.replace( " ","T")).dayOfWeek.toString()
            if(calculted_day.equals((dayOfWeek).toString())) {
                itemView.list_day.text = "TODAY"
            }
            else if(calculted_day.equals((dayOfWeek+1).toString())){
                itemView.list_day.text = "TOMORROW"
            }
            else{
                itemView.list_day.text = calculted_day
            }
            itemView.list_description.setText(data.weather_desc)
            itemView.list_temp.setText(Math.round((data.temp-273.15)).toString()+"°")
        }

    }
}