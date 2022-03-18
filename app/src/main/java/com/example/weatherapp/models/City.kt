package com.example.weatherapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "cities_table")
data class City( @PrimaryKey(autoGenerate = true) val id: Long,
                 @ColumnInfo(name = "city")val name:String)
