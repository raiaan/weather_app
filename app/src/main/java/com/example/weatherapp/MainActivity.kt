package com.example.weatherapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.home_screen.view_model.WeatherViewModel
import com.example.weatherapp.home_screen.view_model.WeatherViewModelFactory
import com.example.weatherapp.models.OnlineRepository
import com.example.weatherapp.network.RetrofitService

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}