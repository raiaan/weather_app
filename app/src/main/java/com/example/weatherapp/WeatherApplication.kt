package com.example.weatherapp

import android.app.Application
import com.example.weatherapp.db.AppRoomDatabase
import com.example.weatherapp.repositories.LocalCityRepo

class WeatherApplication : Application() {
    val database by lazy { AppRoomDatabase.getDatabase(this) }
    val repository by lazy { LocalCityRepo(database.cityDao()) }
}