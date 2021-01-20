package com.weather.bp.data.source.local

import com.weather.bp.data.models.ConsolidatedWeather
import com.weather.bp.data.source.local.dao.WeatherDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeatherLocalDataSourceImpl
@Inject constructor(
    private val weatherDao: WeatherDao,
    private val ioDispatcher: CoroutineDispatcher
) : WeatherLocalDataSource {
    override suspend fun getLatestWeatherForecast(): ConsolidatedWeather? = withContext(ioDispatcher) {
        return@withContext weatherDao.getLatestWeatherForecast()
    }

    override suspend fun saveWeather(weatherData: List<ConsolidatedWeather>) = withContext(ioDispatcher) {
        weatherDao.insertAll(weatherData)
    }

    override suspend fun deleteWeather() = withContext(ioDispatcher) {
        weatherDao.deleteAll()
    }
}
