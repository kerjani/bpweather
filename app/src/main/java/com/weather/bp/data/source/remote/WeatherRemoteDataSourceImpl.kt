package com.weather.bp.data.source.remote

import com.weather.bp.data.models.ConsolidatedWeather
import com.weather.bp.retrofit.ApiService
import com.weather.bp.util.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject


class WeatherRemoteDataSourceImpl
@Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val retrofitClient: ApiService
) : WeatherRemoteDataSource {

    override suspend fun getConsolidatedWeatherData(): Result<List<ConsolidatedWeather>> =
        withContext(ioDispatcher) {
            return@withContext try {
                val result = retrofitClient.getLocationWeather()
                if (result.isSuccessful) {
                    val networkWeather = result.body()?.consolidated_weather
                    Result.Success(networkWeather)
                } else {
                    Result.Success(null)
                }
            } catch (exception: Exception) {
                Result.Error(exception)
            }
        }

}
