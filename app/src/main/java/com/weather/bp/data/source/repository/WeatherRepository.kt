package com.weather.bp.data.source.repository

import com.weather.bp.data.models.ConsolidatedWeather
import com.weather.bp.util.Result

interface WeatherRepository {

    suspend fun getWeatherData(refresh: Boolean): Result<ConsolidatedWeather?>

    suspend fun storeWeatherData(weatherData: ConsolidatedWeather)

    suspend fun deleteWeatherData()

}
