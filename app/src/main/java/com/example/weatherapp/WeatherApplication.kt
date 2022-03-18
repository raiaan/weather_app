package com.example.weatherapp

import android.app.Application
import com.example.weatherapp.db.AppRoomDatabase
import com.example.weatherapp.repositories.LocalCityRepo

class WeatherApplication : Application() {
    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { AppRoomDatabase.getDatabase(this) }
    val repository by lazy { LocalCityRepo(database.cityDao()) }
}