package com.weather.bp.ui.main

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weather.bp.data.models.ConsolidatedWeather
import com.weather.bp.data.source.repository.WeatherRepository
import com.weather.bp.util.RefreshIntervalHelper
import com.weather.bp.util.Result
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    val weatherRepository: WeatherRepository,
    val refreshIntervalHelper: RefreshIntervalHelper
) : ViewModel() {

    val isLoading = MutableLiveData<Boolean>()

    val temperature = MutableLiveData<String>()

    val dataAge = MutableLiveData<String>()

    val weatherName = MutableLiveData<String>()

    var error = MutableLiveData<String>()

    val latestWeatherForecast = MutableLiveData<ConsolidatedWeather>()

    fun getWeather() {
        Log.d(TAG, "Attempt to get stored data")
        isLoading.postValue(true)
        viewModelScope.launch {
            when (val result = weatherRepository.getWeatherData(false)) {
                is Result.Success -> {
                    isLoading.value = false
                    if (result.data != null) {
                        val weather = result.data
                        latestWeatherForecast.value = weather
                        Log.d(TAG, "Stored data has been retrieved successfully")
                        error.value = null
                    } else {
                        Log.d(TAG, "Stored data is missing, refreshing")
                        refreshWeather()
                    }
                }
                is Result.Error -> {
                    isLoading.value = false
                    Log.d(TAG, "Error during data retrieval: ${result.exception}")
                    error.value = result.exception.message
                }

                is Result.Loading -> isLoading.postValue(true)
            }
        }

        if (refreshIntervalHelper.isDataInvalid()) {
            Log.d(TAG, "Weather data is too old, refreshing")
            refreshWeather()
        }
    }

    fun refreshWeather() {
        Log.d(TAG, "Attempt to refresh data")
        isLoading.value = true
        viewModelScope.launch {
            when (val result = weatherRepository.getWeatherData(true)) {
                is Result.Success -> {
                    isLoading.value = false
                    if (result.data != null) {
                        val weather = result.data
                        latestWeatherForecast.value = weather

                        weatherRepository.deleteWeatherData()
                        weatherRepository.storeWeatherData(weather)

                        refreshIntervalHelper.updateRefreshedDate()
                        Log.d(TAG, "Refresh of the data is successful")
                        error.value = null
                    } else {
                        Log.d(TAG, "Refreshed data is null!")
                        error.value = "No data found"
                    }
                }
                is Result.Error -> {
                    Log.d(TAG, "Error during data refresh: ${result.exception}")
                    isLoading.value = false
                    error.value = result.exception.message
                }
                is Result.Loading -> isLoading.postValue(true)
            }
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}