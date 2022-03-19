package com.example.weatherapp.db

import androidx.room.*
import com.example.weatherapp.models.Alert
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun alarmInsert(alert: Alert)

    @Query("SELECT * FROM Alerts")
    fun getalerts(): Flow<List<Alert>>

    @Delete
    suspend fun deleteAlerts(alert : Alert)
}