package com.example.weatherapp.alert.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.models.Alert
import com.example.weatherapp.models.Hourly
import com.example.weatherapp.repositories.LocalAlertRepo
import com.example.weatherapp.util.getDateTime
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AlertViewModel(private val repository: LocalAlertRepo) : ViewModel() {
    val getAlert: LiveData<List<Alert>> = repository.getAlerts.asLiveData()
    fun delete(alert: Alert) = viewModelScope.launch {
        repository.delete(alert)
    }
    fun addAlertToDB(dateFrom: String, dateTo: String, event: String, requestCode: Int){
        viewModelScope.launch {
            repository.insert(Alert(dateFrom ,dateTo ,event,requestCode))
        }
    }
    fun convertAndCheck(date: String, startTime: String, endTime: String): Boolean {
        val dateFormat = SimpleDateFormat("MM/dd/yyyy hh:mm:ss")
        var convertedDate: Date?
        var convertedDate2: Date?
        var convertedDate3: Date?
        try {
            convertedDate = dateFormat.parse(date)
            convertedDate2 = dateFormat.parse(startTime)
            convertedDate3 = dateFormat.parse(endTime)
            if ((convertedDate2.before(convertedDate)&&convertedDate3.after(convertedDate))
                ||convertedDate2.equals(convertedDate)||convertedDate3.equals(convertedDate)) {
                return true
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return false
    }
    fun search11( alarmHours: List<Hourly>, startTime: String, endTime: String, event: String): Hourly? {
        var i: Int = 0
        while (i < alarmHours.size) {
            var timeInList = getDateTime(alarmHours.get(i).dt.toString(), "MM/dd/yyyy hh:mm:ss")
            if (convertAndCheck(timeInList!!, startTime!!, endTime)
                && alarmHours.get(i).weather.get(0).main.equals(event))
            {
                return alarmHours.get(i)
            } else {
                i++
            }
        }
        return null
    }
}