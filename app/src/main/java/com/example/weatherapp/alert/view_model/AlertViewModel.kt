package com.example.weatherapp.alert.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.weatherapp.models.Alert
import com.example.weatherapp.repositories.LocalAlertRepo
import com.example.weatherapp.worker.AlertWorker
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit


class AlertViewModel(private val repository: LocalAlertRepo) : ViewModel() {
    val getAlert: LiveData<List<Alert>> = repository.getAlerts.asLiveData()
    fun delete(alert: Alert) = viewModelScope.launch {
        repository.delete(alert)
    }
    fun addAlertToDB(time: Long, event: String, requestCode: Int){
        viewModelScope.launch {
            repository.insert(Alert(time ,event,requestCode))
        }
    }
    fun addToWorkManager(time: Long, event: String, requestCode: Int,context: Context){
        val data= Data.Builder().putString("Alert_event", event).build()
        val currentDate = Date().time
        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(AlertWorker::class.java)
            .addTag("Weather Alerts")
            .setInitialDelay(currentDate - time, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .build()
        val workManager = WorkManager.getInstance(context)
        workManager.enqueue(oneTimeWorkRequest)
    }

}