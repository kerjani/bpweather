package com.weather.bp.util

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import javax.inject.Inject

class SharedPreferencesHelper @Inject constructor(context: Context) {
    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    var latestRefreshDate: Long
        get() = prefs.getLong(LATEST_REFRESH_DATE_PREF, 0L)
        set(time) {
            prefs.edit(commit = true) {
                putLong(LATEST_REFRESH_DATE_PREF, time)
            }
        }

    companion object {
        private const val LATEST_REFRESH_DATE_PREF = "LatestRefreshDate"
    }


}
