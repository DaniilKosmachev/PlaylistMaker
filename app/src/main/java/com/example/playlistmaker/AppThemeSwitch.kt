package com.example.playlistmaker

import android.content.SharedPreferences

class AppThemeSwitch(private val sharedPreferences: SharedPreferences) {

    fun getStatusSwitchFromShared(): Boolean {
        return sharedPreferences.getBoolean(APP_THEME_SWITCH_SETTINGS, false)
    }

    fun writeStatusSwitchToShared(status: Boolean) {
        sharedPreferences.edit()
            .putBoolean(APP_THEME_SWITCH_SETTINGS, status)
            .apply()
    }

    companion object {
        const val APP_THEME_SWITCH_SETTINGS = "theme_switch_status"
    }
}