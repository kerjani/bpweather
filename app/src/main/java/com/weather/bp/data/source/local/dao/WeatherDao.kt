package com.weather.bp.data.source.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.weather.bp.data.models.ConsolidatedWeather

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(weatherData: List<ConsolidatedWeather>)

    @Query("SELECT * FROM consolidatedweather ORDER BY created DESC LIMIT 1")
    fun getWeather(): LiveData<ConsolidatedWeather> // TODO delete

    @Query("SELECT * FROM consolidatedweather ORDER BY created DESC LIMIT 1")
    suspend fun getLatestWeatherForecast(): ConsolidatedWeather

    @Query("DELETE FROM consolidatedweather")
    suspend fun deleteAll()
}
