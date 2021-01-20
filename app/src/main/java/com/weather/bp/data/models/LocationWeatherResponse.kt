package com.weather.bp.data.models

import androidx.room.Entity

@Entity(tableName = "locationweatherresponse")
data class LocationWeatherResponse(
    val consolidated_weather: List<ConsolidatedWeather>,
    val latt_long: String,
    val location_type: String,
    val sun_rise: String,
    val sun_set: String,
    val time: String,
    val timezone: String,
    val timezone_name: String,
    val title: String,
    val woeid: Int
)