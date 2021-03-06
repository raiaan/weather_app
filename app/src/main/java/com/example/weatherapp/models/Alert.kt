package com.example.weatherapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Alerts")
class Alert (
    @ColumnInfo(name = "date") val date: Long,
    @ColumnInfo(name = "event") val event: String,
    @ColumnInfo(name = "requestCode") val requestCode: Int
    ){
    @PrimaryKey(autoGenerate = true) var alarm_id: Int = 0
}