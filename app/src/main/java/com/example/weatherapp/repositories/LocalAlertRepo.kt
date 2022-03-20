package com.example.weatherapp.repositories

import androidx.annotation.WorkerThread
import com.example.weatherapp.db.AlertDao
import com.example.weatherapp.models.Alert
import com.example.weatherapp.models.City
import kotlinx.coroutines.flow.Flow

class LocalAlertRepo(private val alertDao: AlertDao) {
    val getAlerts: Flow<List<Alert>> = alertDao.getalerts()
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(alert: Alert) {
        alertDao.alarmInsert(alert)
    }
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(alert: Alert) {
        alertDao.deleteAlerts(alert)
    }
}