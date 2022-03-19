package com.example.weatherapp.favourite.view

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.example.weatherapp.databinding.FragmentFavouritesBinding
import com.example.weatherapp.models.City

class MyItemRecyclerViewAdapter(val deleteCallBack: (city:City)->Unit , val itemClickCallback:(city:City)->Unit) : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {
    var city = listOf<City>()
    @SuppressLint("NotifyDataSetChanged")
    set(value) {
        field = value
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentFavouritesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = city[position]
        holder.contentView.text = item.name
        holder.itemView.setOnClickListener {
            itemClickCallback(item)
        }
        holder.binding.removeCity.setOnClickListener {
            deleteCallBack(item)
        }

    }

    override fun getItemCount(): Int = city.size

    inner class ViewHolder(val binding: FragmentFavouritesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val contentView: TextView = binding.content
    }

}