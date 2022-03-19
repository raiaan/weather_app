package com.example.weatherapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "cities_table")
data class City( @PrimaryKey(autoGenerate = true) val id: Long,
                 @ColumnInfo(name = "city")val name:String,
                 @ColumnInfo(name = "lat")val lat:String,
                 @ColumnInfo(name = "lon")val lon:String
):Serializable
