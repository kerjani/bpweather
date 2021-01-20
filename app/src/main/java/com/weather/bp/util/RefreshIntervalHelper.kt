package com.weather.bp.util

interface RefreshIntervalHelper {
    fun isDataInvalid(): Boolean
    fun updateRefreshedDate()
}
