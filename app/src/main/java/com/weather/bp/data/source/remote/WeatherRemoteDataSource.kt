package com.weather.bp.data.source.remote

import com.weather.bp.data.models.ConsolidatedWeather
import com.weather.bp.util.Result

interface WeatherRemoteDataSource {
    suspend fun getConsolidatedWeatherData(): Result<List<ConsolidatedWeather>>
}
