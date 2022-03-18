package com.example.weatherapp.home_screen.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.databinding.DailyForcasteItemBinding
import com.example.weatherapp.models.Daily
import com.example.weatherapp.util.dateFromLongToStr

class DailyAdapter: RecyclerView.Adapter<DailyAdapter.DaysViewHolder>() {
    var days =  mutableListOf<Daily>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DaysViewHolder {
        val binding = DailyForcasteItemBinding.inflate( LayoutInflater.from(parent.context),
            parent, false)
        return DaysViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DaysViewHolder, position: Int) {
        Glide.with(holder.itemView.context)
            .load("https://openweathermap.org/img/wn/${days[position].weather[0].icon}.png")
            .into(holder.binding.currentWeatherIcon)
        holder.binding.dayDate.text = dateFromLongToStr(days[position].dt!! ,"EEE MMM d")
        holder.binding.currentWeatherFeelLike.text = days[position].weather[0].description
        holder.binding.currentWeatherTemp.text = "${days[position].temp!!.min}"
        holder.binding.currentWeatherHumidity.text = "Humidity: ${days[position].humidity}%"
        holder.binding.currentWeatherWind.text = "Wind: ${days[position].windSpeed}"
        holder.binding.currentWeatherPressure.text = "Pressure: ${days[position].pressure}"
        holder.binding.currentWeatherClouds.text = "Clouds: ${days[position].clouds}%"
    }

    override fun getItemCount(): Int {
        return days.size
    }

    class DaysViewHolder(val binding: DailyForcasteItemBinding) : RecyclerView.ViewHolder(binding.root) {
    }
}