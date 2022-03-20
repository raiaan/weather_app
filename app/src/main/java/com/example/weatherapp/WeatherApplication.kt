package com.example.weatherapp

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.weatherapp.db.AppRoomDatabase
import com.example.weatherapp.repositories.LocalAlertRepo
import com.example.weatherapp.repositories.LocalCityRepo

class WeatherApplication : Application() {
    val database by lazy { AppRoomDatabase.getDatabase(this) }
    val repository by lazy { LocalCityRepo(database.cityDao()) }
    val alertRepo by lazy { LocalAlertRepo(database.AlertDao()) }
}