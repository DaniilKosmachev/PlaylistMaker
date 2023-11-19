package com.example.playlistmaker.data.settings.impl

import android.app.Application
import android.content.Context.MODE_PRIVATE
import com.example.playlistmaker.App
import com.example.playlistmaker.data.settings.AppThemeRepository
import java.io.Serializable

class AppThemeRepositoryImpl(app: Application): AppThemeRepository, Serializable {

    var app = app as App

    var sharedPreferences = app.getSharedPreferences(APP_THEME_SHARED_FILE_NAME, MODE_PRIVATE)

    override fun getStatusSwitchFromShared(): Boolean {
        return sharedPreferences.getBoolean(APP_THEME_SWITCH_SETTINGS, false)

    }

    override fun writeStatusSwitchToShared(status: Boolean) {
        sharedPreferences.edit()
            .putBoolean(APP_THEME_SWITCH_SETTINGS, status)
            .apply()
        app.switchTheme(status)
    }

    companion object {
        const val APP_THEME_SHARED_FILE_NAME = "theme_shared_preferences"
        const val APP_THEME_SWITCH_SETTINGS = "theme_switch_status"
    }

}