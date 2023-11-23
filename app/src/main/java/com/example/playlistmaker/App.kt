package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.settings.AppThemeInteractor

var darkTheme = false

class App : Application() {

    private lateinit var themeSwitch: AppThemeInteractor

    override fun onCreate() {
        super.onCreate()
        Creator.initApplication(this)
        themeSwitch = Creator.provideAppThemeInteractor()
        switchTheme(themeSwitch.getStatusSwitchFromShared())
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
    }

}