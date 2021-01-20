package com.weather.bp.util

import javax.inject.Inject

class RefreshIntervalHelperIml @Inject constructor(
    private val sharedPreferencesHelper: SharedPreferencesHelper
) : RefreshIntervalHelper {

    override fun isDataInvalid(): Boolean {
        val currentTime = System.currentTimeMillis()
        val lastUpdate = sharedPreferencesHelper.latestRefreshDate
        return lastUpdate > 0L && currentTime - lastUpdate > Constants.REFRESH_INTERVAL_IN_MILLIS
    }

    override fun updateRefreshedDate() {
        sharedPreferencesHelper.latestRefreshDate = System.currentTimeMillis()
    }

}