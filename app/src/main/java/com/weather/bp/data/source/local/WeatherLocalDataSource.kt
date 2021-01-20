package com.weather.bp.data.source.local

import com.weather.bp.data.models.ConsolidatedWeather


interface WeatherLocalDataSource {

    suspend fun getLatestWeatherForecast(): ConsolidatedWeather?

    suspend fun saveWeather(weatherData: List<ConsolidatedWeather>)

    suspend fun deleteWeather()
}
