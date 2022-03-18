package com.example.weatherapp.db

import androidx.room.*
import androidx.room.Dao
import com.example.weatherapp.models.City
import kotlinx.coroutines.flow.Flow


@Dao
interface WeatherDao {
    @Query("SELECT * FROM cities_table ORDER BY city ASC")
    fun getAlphabetizedWords(): Flow<List<City>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(city: City)

    @Delete
    suspend fun delete(city: City)
}