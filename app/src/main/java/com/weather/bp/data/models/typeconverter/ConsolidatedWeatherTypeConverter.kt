package com.weather.bp.data.models.typeconverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.weather.bp.data.models.ConsolidatedWeather

class ConsolidatedWeatherTypeConverter {
    @TypeConverter
    fun fromConsolidatedWeathersListToString(value: List<ConsolidatedWeather>): String {
        var result = ""
        for (weather in value) result += "${Gson().toJson(weather)},"
        return result
    }

    @TypeConverter
    fun fromListToConsolidatedWeathers(value: String): List<ConsolidatedWeather> {
        val weathers: List<String> = value.split("\\s*,\\s*")
        val consolidatedWeathers = mutableListOf<ConsolidatedWeather>()
        for (text in weathers) {
            consolidatedWeathers.add(Gson().fromJson(text, ConsolidatedWeather::class.java))
        }
        return consolidatedWeathers
    }
}
