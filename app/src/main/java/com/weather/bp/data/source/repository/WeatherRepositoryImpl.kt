package com.weather.bp.data.source.repository

import com.weather.bp.data.models.ConsolidatedWeather
import com.weather.bp.data.source.local.WeatherLocalDataSource
import com.weather.bp.data.source.remote.WeatherRemoteDataSource
import com.weather.bp.util.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val remoteDataSource: WeatherRemoteDataSource,
    private val localDataSource: WeatherLocalDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : WeatherRepository {

    override suspend fun getWeatherData(refresh: Boolean): Result<ConsolidatedWeather> =
        withContext(ioDispatcher) {
            if (refresh) {
                when (val response = remoteDataSource.getConsolidatedWeatherData()) {
                    is Result.Success -> {
                        Result.Success(response.data?.get(0))
                    }

                    is Result.Error -> {
                        Result.Error(response.exception)
                    }

                    else -> Result.Loading
                }
            } else {
                val forecast = localDataSource.getLatestWeatherForecast()
                Result.Success(forecast)
            }
        }

    override suspend fun storeWeatherData(weatherData: ConsolidatedWeather) =
        withContext(ioDispatcher) {
            localDataSource.saveWeather(listOf(weatherData))
        }

    override suspend fun deleteWeatherData() = withContext(ioDispatcher) {
        localDataSource.deleteWeather()
    }

}
