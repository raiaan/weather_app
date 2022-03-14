package com.example.weatherapp.models

import com.example.weatherapp.network.RetrofitService

class OnlineRepository (val retrofitService:RetrofitService ,val lat:String ,val long:String) {
    suspend fun getWeatherData() = retrofitService.allWeatherData(lat,long)
}