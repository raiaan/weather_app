package com.example.weatherapp.models

import com.google.gson.annotations.SerializedName


data class Minutely (
  @SerializedName("dt"            ) var dt            : Int? = null,
  @SerializedName("precipitation" ) var precipitation : Double? = null
)