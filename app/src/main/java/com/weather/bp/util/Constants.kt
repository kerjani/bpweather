package com.weather.bp.util

import java.util.concurrent.TimeUnit

object Constants {
    const val REFRESH_INTERVAL_IN_MINUTES = 1L
    val REFRESH_INTERVAL_IN_MILLIS = TimeUnit.MINUTES.toMillis(REFRESH_INTERVAL_IN_MINUTES)
    const val DATE_FORMAT = "yyyy-M-dd HH:mm:ss"
    const val BASE_URL = "https://www.metaweather.com/"
    const val BASE_API_URL = "${BASE_URL}api/"
}