package com.example.weatherapp.repositories

import androidx.annotation.WorkerThread
import com.example.weatherapp.db.WeatherDao
import com.example.weatherapp.models.City
import kotlinx.coroutines.flow.Flow

class LocalCityRepo (private val cityDao: WeatherDao) {
    val allCities: Flow<List<City>> = cityDao.getAlphabetizedWords()
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(city: City) {
        cityDao.insert(city)
    }
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(city: City) {
        cityDao.delete(city)
    }

}