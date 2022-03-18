package com.example.weatherapp.repositories

import com.example.weatherapp.network.RetrofitService

class OnlineRepository (private val retrofitService:RetrofitService, private val lat:String, private val long:String) {
    suspend fun getWeatherData() = retrofitService.allWeatherData(lat,long)
}