package com.weather.bp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "consolidatedweather")
data class ConsolidatedWeather(
    val created: String,
    @PrimaryKey
    val id: Long,
    val the_temp: Double,
    val weather_state_abbr: String,
    val weather_state_name: String,
)