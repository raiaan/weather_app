package com.example.weatherapp.home_screen.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.databinding.DailyHoursItemsBinding
import com.example.weatherapp.models.Hourly
import com.example.weatherapp.util.dateFromLongToStr
import java.text.SimpleDateFormat
import java.util.*


class HoursAdapter: RecyclerView.Adapter<HoursAdapter.HoursViewHolder>() {
    var hours =  mutableListOf<Hourly>()
    set(value) {
        field = value
        notifyDataSetChanged()
        Log.v("change","${field.size}")
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoursViewHolder {
        val binding = DailyHoursItemsBinding.inflate( LayoutInflater.from(parent.context),
            parent, false)
        return HoursViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HoursViewHolder, position: Int) {
        holder.binding.hoursTimeWeather.text = "${hours[position].temp}"
        holder.binding.hoursTxt.text = dateFromLongToStr(hours[position].dt!!,"HH:mm aa")
        Glide.with(holder.itemView.context)
            .load("https://openweathermap.org/img/wn/${hours[position].weather[0].icon}.png")
            .into(holder.binding.hourWeatherIcon)
    }

    override fun getItemCount(): Int {
        return hours.size
    }
    class HoursViewHolder(val binding: DailyHoursItemsBinding) : RecyclerView.ViewHolder(binding.root) {
    }
}