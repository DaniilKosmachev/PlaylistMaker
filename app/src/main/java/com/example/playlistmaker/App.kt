package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

var darkTheme = false

class App : Application() {
    private lateinit var themeSwitchSharedPreferences: SharedPreferences
    private lateinit var classThemeSwitch: AppThemeSwitch

    override fun onCreate() {
        super.onCreate()
        themeSwitchSharedPreferences = getSharedPreferences(THEME_SWITCH_FILE_NAME, MODE_PRIVATE)
        classThemeSwitch = AppThemeSwitch(themeSwitchSharedPreferences)
        switchTheme(classThemeSwitch.getStatusSwitchFromShared())
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        classThemeSwitch.writeStatusSwitchToShared(darkThemeEnabled)
    }

    companion object {
        const val THEME_SWITCH_FILE_NAME = "theme_shared_preferences"
    }

}